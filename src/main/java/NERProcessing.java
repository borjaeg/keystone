import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

public class NERProcessing {

    public static void process(String abstracts) {
        String serializedClassifier = "stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        System.out.println(classifier.classifyToString(abstracts));
        System.out.println(classifier.classifyWithInlineXML(abstracts));
    }
}

