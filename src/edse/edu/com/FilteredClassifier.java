/*
 * Author: Justin Edse
 * Title: FilteredClassifier.java
 * Purpose: This class uses the previous model built in the FilteredLearner.java class and classifies each instance it
 * sees as either male or female.
 * Date: August, September 2013
 */

package edse.edu.com;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

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
import weka.classifiers.trees.J48;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class FilteredClassifier
{

	String name = "";
	String scrname = "";
	String desc = "";

	/**
	 * String that stores the text to classify
	 */
	String text;
	/**
	 * Object that stores the instance.
	 */
	Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	weka.classifiers.meta.FilteredClassifier classifier;

	/**
	 * This method loads the text to be classified.
	 * 
	 * @param fileName
	 *            The name of the file that stores the text.
	 */
	public void load(String fileName)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			text = "";
			while ((line = reader.readLine()) != null)
			{
				text = text + " " + line;
			}
			System.out
					.println("===== Loaded text data: " + fileName + " =====");
			reader.close();
			System.out.println(text);
		} catch (IOException e)
		{
			System.out.println("Problem found when reading: " + fileName);
		}
	}

	/**
	 * This method loads the model to be used as classifier.
	 * 
	 * @param fileName
	 *            The name of the file that stores the text.
	 */
	public void loadModel(String fileName)
	{
		try
		{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					fileName));
			Object tmp = in.readObject();
			classifier = (weka.classifiers.meta.FilteredClassifier) tmp;
			in.close();
			System.out.println("===== Loaded model: " + fileName + " =====");
		} catch (Exception e)
		{
			// Given the cast, a ClassNotFoundException must be caught along
			// with the IOException
			System.out.println("Problem found when reading: " + fileName);
		}
	}

	/**
	 * This method creates a new instance for the object that needs to classified.
	 * In the same format as the ARFF file, the instance fields are laid out in just 
	 * order just like the parameters are in the method below.
	 * @param nameParam The user's real name
	 * @param scName The user's screen name
	 * @param userDesc The user's profile description
	 * @param newTextInstance The user's text for their tweet
	 */
	public void makeInstance(String nameParam, String scName, String userDesc,
			String newTextInstance)
	{

		name = nameParam;
		scrname = scName;
		desc = userDesc;
		text = newTextInstance;

		// Create the attributes: 0 | 1, real name | scname | desc | text |
		// class val
		FastVector fvNominalVal = new FastVector(2);
		fvNominalVal.addElement("MALE");
		fvNominalVal.addElement("FEMALE");
		Attribute attribute1 = new Attribute("name", (FastVector) null);
		Attribute attribute2 = new Attribute("scrname", (FastVector) null);
		Attribute attribute3 = new Attribute("desc", (FastVector) null);
		Attribute attribute4 = new Attribute("text", (FastVector) null);
		Attribute attribute5 = new Attribute("class", fvNominalVal);

		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(5);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		fvWekaAttributes.addElement(attribute3);
		fvWekaAttributes.addElement(attribute4);
		fvWekaAttributes.addElement(attribute5);

		instances = new Instances("Test relation", fvWekaAttributes, 1);
		// Set class index
		instances.setClassIndex(instances.numAttributes() - 1);
		// Create and add the instance

		Instance instance = new Instance(5);
		instance.setValue(attribute1, name);
		instance.setValue(attribute2, scrname);
		instance.setValue(attribute3, desc);
		instance.setValue(attribute4, text);

		instances.add(instance);
		System.out
				.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
	}

	/**
	 * This method performs the classification of the instance. Output is done
	 * at the command-line.
	 */
	public String classify()
	{
		double pred = 0.0;
		try
		{
			pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: "
					+ instances.classAttribute().value((int) pred));
		} catch (Exception e)
		{
			System.out.println("Problem found when classifying the text");
		}

		return instances.classAttribute().value((int) pred);

	}

	/**
	 * Main method. It is an example of the usage of this class.
	 * 
	 * @param args
	 *            Command-line arguments: fileData and fileModel.
	 */
	public static String classifyNewInstance(String uName, String uScrn,
			String desc, String newText)
	{

		FilteredClassifier classifier;
		String result = null;

		// The classification of a new instance begins with loading the ARFF
		// file and then loading
		// the saved model that was produced during the learning phase.
		classifier = new FilteredClassifier();
		classifier.load("C://Users//edse4_000//Desktop//usersnext.arff");
		classifier.loadModel("C://outgend.model");

		classifier.makeInstance(uName, uScrn, desc, newText);

		result = classifier.classify();

		// Here the String result of MALE or FEMALE is returned to the
		// GenderClassification.java class.
		return result;
	}

}