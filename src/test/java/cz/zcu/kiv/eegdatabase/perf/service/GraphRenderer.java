/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   GraphRenderer.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.service;

import org.databene.contiperf.ExecutionLogger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import java.util.Map.Entry;
/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 * Class generate char from test result.
 */

public class GraphRenderer implements ExecutionLogger{


    public GraphRenderer(){

    }

	/**
	 * Map -save data for graph.
	 */
	Map<Integer, Integer> mapa = new HashMap<Integer, Integer>();

	private String method;


	public void logInvocation(String arg0, int arg1, long arg2) {
		//System.out.println("zavola se metoda log..");
		System.out.println("log = "+arg0 + " " + arg1 + " " + arg2);
		method = arg0;
		Integer i = mapa.get(arg1);
		mapa.put(arg1, i == null ? 1 : i + 1);

	}

	public void logSummary(String arg0, long arg1, long arg2, long arg3) {


	}
    /**
     * Method generate graph and save as name of test method.
	 */
	public void save(){
	    	System.out.println("mapa ="+mapa);
	    	XYSeries series = new XYSeries("Execution time occurance");
	    	Set<Entry<Integer,Integer>> entries = mapa.entrySet();
	    	for (Entry<Integer, Integer> entry : entries) {
	    	    series.add(new XYDataItem(entry.getKey(), entry.getValue()));
	    	}
//	    	series.add(20.0, 10.0);
//	    	series.add(40.0, 20.0);
//	    	series.add(70.0, 50.0);

	    	String plotTitle = method;
            //String plotTitle = "report";
	    	String xaxis = "Elapsed time";
	    	String yaxis = "Occurance";
	    	PlotOrientation orientation = PlotOrientation.VERTICAL;
	    	boolean show = false;
	    	boolean toolTips = false;
	    	boolean urls = false;
	    	JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis,
	    		yaxis, new XYSeriesCollection(series), orientation, show, toolTips, urls);
	    	int width = 1024;
	    	int height = 768;
	    	try {
	    	    ChartUtilities.saveChartAsPNG(new File(method+".png"), chart,
	    		    width, height);
	    	    System.out.println("graf vygenerovan...."+method+".png");
	    	} catch (IOException e) {

	    }

	    }


}

