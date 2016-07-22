$(document).ready(function() {

    var earth;
    var isMicrophoneActive = true;

    function flyToJapan() {
        earth.fitBounds([
            [22, 122],
            [48, 154]
        ]);
        earth.panInsideBounds([
            [22, 122],
            [48, 154]
        ], {
            heading: 90,
            tilt: 25,
            duration: 1
        });
    }

    $("#move").click(function() {
        flyToJapan();
    });

    function initialize() {
        earth = new WE.map('earth_div');
        earth.setView([46.8011, 8.2266], 2);
        /*WE.tileLayer('http://data.webglearth.com/natural-earth-color/{z}/{x}/{y}.jpg', {
          tileSize: 256,
          bounds: [[-85, -180], [85, 180]],
          minZoom: 0,
          maxZoom: 16,
          attribution: 'WebGLEarth example',
          tms: true
        }).addTo(earth);*/
        WE.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(earth);


        // voice recognition
        var recognition = new webkitSpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        $('#activateMicrophone').click(function() {
            if (isMicrophoneActive){
                recognition.stop();
                $(this).toggleClass('saturate');
                isMicrophoneActive = false;
            }
            else {
                recognition.start();
                isMicrophoneActive = true;
            }
        });

        var markers = [];
        $("input").on("keydown", function search(e) {
            if (e.keyCode == 13) {
                for (var i = 0; i < markers.length; i++)
                    earth.removeMarker(markers[i]);
                if ($("#season").length>1){
                    var path = "path?name=" + $("#keywords").val() + "&season=" + $("#season").val();
                } else {
                    var path = "path?name=" + $("#keywords").val();
                }
                $.get(path, function(data) {
                    console.log(data);
                    for (var i = 0; i < data.length; i++) {
                        var marker = WE.marker([data[i].lat, data[i].lon]).addTo(earth);
                        markers.push(marker);
                        marker.bindPopup("<b>" + data[i].name + "</b>", {
                            maxWidth: 150,
                            closeButton: true
                        });
                    }
                });
            }
        });

        recognition.onresult = function(e) {
                    for (var i = 0; i < markers.length; i++)
                        earth.removeMarker(markers[i]);
                    var textarea = document.getElementById('keywords');
                    textarea.value = "";
                    for (var i = e.resultIndex; i < e.results.length; ++i) {
                        if (e.results[i].isFinal) {
                            textarea.value += e.results[i][0].transcript;
                            console.log(e.results[i][0].transcript);
                            if ($("#season").length>1){
                                var path = "path?name=" + e.results[i][0].transcript + "&season=" + $("#season").val();
                            } else {
                                var path = "path?name=" + e.results[i][0].transcript;
                            }

                            $.get(path, function(data) {
                                console.log(data);
                                for (var i = 0; i < data.length; i++) {
                                    var marker = WE.marker([data[i].lat, data[i].lon]).addTo(earth);
                                    markers.push(marker);
                                    marker.bindPopup("<b>" + data[i].name + "</b>", {
                                        maxWidth: 150,
                                        closeButton: true
                                    });
                                }
                            });
                        }
                    }
            };
            // start listening
        recognition.start();
    }

    initialize();
});