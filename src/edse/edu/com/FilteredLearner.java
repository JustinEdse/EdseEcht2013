/*
 * Author: Justin Edse
 * Title: FilteredLearner.java
 * Purpose: To bring in an ARFF file type, make the needed number of instances and build a classifier based
 * on that information. After this the model created is saved on the C drive for later use in the FilteredClassifier.java class. Had to
 * search through numerous forums and tutorials online on how to do this with Weka. Some of the very helpful pages were:
 * http://weka.wikispaces.com/Creating+an+ARFF+file
 * http://ianma.wordpress.com/2010/01/16/weka-with-java-eclipse-getting-started/
 * http://weka.wikispaces.com/Programmatic+Use
 * http://comments.gmane.org/gmane.comp.ai.weka/21199
 * http://www.hakank.org/weka/
 * http://jmgomezhidalgo.blogspot.com/2013/04/a-simple-text-classifier-in-java-with.html
 * Date: August, September 2013
 */
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

			trainData.setClassIndex(trainData.numAttributes() - 1);

			// Splitting the text attributes into a vector of words here.
			filter = new weka.filters.unsupervised.attribute.StringToWordVector();


			classifier = new FilteredClassifier();

			classifier.setFilter(filter);

			// Picking the Naive Bayes Multinomial as the classifier since it's supposed
			// to be better at handling text than just regular Naive Bayes.
			classifier
					.setClassifier(new weka.classifiers.bayes.NaiveBayesMultinomial());

			Evaluation eval = new Evaluation(trainData);

			// performing cross validation here with the training data
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

			trainData.setClassIndex(trainData.numAttributes() - 1);
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
	 * This method initiates the actions of the class.
	 */
	public static void init()
	{

		FilteredLearner learner;

		//loading the dataset and saving the model here.
		learner = new FilteredLearner();
		learner.loadDataset("C://Users//edse4_000//Desktop//usersnext.arff");
		learner.evaluate();
		learner.learn();
		learner.saveModel("C://outgend.model");
	}

}