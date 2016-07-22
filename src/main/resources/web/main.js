$( document ).ready(function() {

var earth;

function flyToJapan() {
        earth.fitBounds([[22, 122], [48, 154]]);
        earth.panInsideBounds([[22, 122], [48, 154]],
                            {heading: 90, tilt: 25, duration: 1});
}

$("#move").click(function(){
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
        /*var marker = WE.marker([51.5, -0.09]).addTo(earth);
        marker.bindPopup("<b>Hello world!</b><br>I am a popup.<br /><span style='font-size:10px;color:#999'>Tip: Another popup is hidden in Cairo..</span>", {maxWidth: 150, closeButton: true}).openPopup();

        var marker2 = WE.marker([30.058056, 31.228889]).addTo(earth);
        marker2.bindPopup("<b>Cairo</b><br>Yay, you found me!", {maxWidth: 120, closeButton: false});

        var markerCustom = WE.marker([50, -9], '/img/logo-webglearth-white-100.png', 100, 24).addTo(earth);*/

        // voice recognition
    var recognition = new webkitSpeechRecognition();
    recognition.continuous = true;
    recognition.interimResults = true;
 
    recognition.onresult = function (e) {
        var textarea = document.getElementById('keywords');
        textarea.value = "";
        for (var i = e.resultIndex; i < e.results.length; ++i) {
            if (e.results[i].isFinal) {
                textarea.value += e.results[i][0].transcript;
                console.log(e.results[i][0].transcript);
                var path = "path?name=" + e.results[i][0].transcript + "&season=2005";
                $.get( path, function( data ) {
                    console.log(data);
                  for (var i = 0; i < data.length; i++){
                    var marker = WE.marker([data[i].lat, data[i].lon]).addTo(earth);
                    marker.bindPopup("<b>"+ data[i].name +"</b>", {maxWidth: 150, closeButton: true});
                  }
                });
            }
        }
    }
    // start listening
        recognition.start();
    }

    initialize();
});


