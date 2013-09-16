package edse.edu.com;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.FastVector;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffLoader.ArffReader;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.util.Random;

public class FilteredLearner
{

	/**
	 * Object that stores training data.
	 */
	Instances trainData;
	/**
	 * Object that stores the filter
	 */
	StringToWordVector filter;
	/**
	 * Object that stores the classifier
	 */
	FilteredClassifier classifier;

	/**
	 * This method loads a dataset in ARFF format. If the file does not exist,
	 * or it has a wrong format, the attribute trainData is null.
	 * 
	 * @param fileName
	 *            The name of the file that stores the dataset.
	 */
	public void loadDataset(String fileName)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			System.out.println("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		} catch (IOException e)
		{
			System.out.println("Problem found when reading: " + fileName);
		}
	}

	/**
	 * This method evaluates the classifier. As recommended by WEKA
	 * documentation, the classifier is defined but not trained yet. Evaluation
	 * of previously trained classifiers can lead to unexpected results.
	 */
	public void evaluate()
	{
		try
		{
			
			trainData.setClassIndex(trainData.numAttributes() -1);
			// weka.filters.unsupervised.attribute.StringToWordVector
			filter = new weka.filters.unsupervised.attribute.StringToWordVector();
			
			//filter.setAttributeIndices("last");
			
			classifier = new FilteredClassifier();
		
			classifier.setFilter(filter);
			
			classifier.setClassifier(new weka.classifiers.bayes.NaiveBayesMultinomial());
			
			Evaluation eval = new Evaluation(trainData);
			
			eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			
			System.out.println(eval.toSummaryString());
			
			System.out.println(eval.toClassDetailsString());
			System.out
					.println("===== Evaluating on filtered (training) dataset done =====");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method trains the classifier on the loaded dataset.
	 */
	public void learn()
	{
		try
		{
			
			trainData.setClassIndex(trainData.numAttributes() -1);
			Classifier naiveBayes = new weka.classifiers.bayes.NaiveBayesMultinomial();
			
			StringToWordVector stringToWordVector = new StringToWordVector();
			
			classifier = new FilteredClassifier();
			
			classifier.setClassifier(naiveBayes);
			classifier.setFilter(stringToWordVector);
			
			classifier.buildClassifier(trainData);
			
			
			
		
			System.out
			.println("===== Training on filtered (training) dataset done =====");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method saves the trained model into a file. This is done by simple
	 * serialization of the classifier object.
	 * 
	 * @param fileName
	 *            The name of the file that will store the trained model.
	 * 
	 *            weka.filters.unsupervised.attribute.StringToWordVector
	 */
	public void saveModel(String fileName)
	{
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(fileName));
			out.writeObject(classifier);
			out.close();
			System.out.println("===== Saved model: " + fileName + " =====");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Main method. It is an example of the usage of this class.
	 * 
	 * @param args
	 *            Command-line arguments: fileData and fileModel.
	 */
	public static void init()
	{

		FilteredLearner learner;

		learner = new FilteredLearner();
		learner.loadDataset("C://Users//edse4_000//Desktop//usersnext.arff");
		// Evaluation must be done before training
		// More info in: http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
		learner.evaluate();
		learner.learn();
		learner.saveModel("C://outgend.model");
	}

}