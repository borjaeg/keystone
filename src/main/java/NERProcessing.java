import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;

public class NERProcessing {

    private static AbstractSequenceClassifier classifier;

    public static String process(String abstracts) {
        String serializedClassifier = "stanford-ner/classifiers/english.all.3class.distsim.crf.ser.gz";
        classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

        return classifier.classifyWithInlineXML(abstracts);
    }
}

