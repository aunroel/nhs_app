//
//  StepsGraphs.swift
//  GapdarMyPages
//
//  Created by localadmin on 04/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit
import Charts
import Foundation

class Graph: ChartViewDelegate{
    var chartView: CombinedChartView
    var type: String
    
    let defaults = UserDefaults.standard
    let weeks = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]
    let ITEM_COUNT = 9
    
    
    init(chartView: CombinedChartView, type: String){
        self.chartView = chartView
        self.type = type
        
        chartView.delegate = self
        chartView.drawGridBackgroundEnabled = false
        chartView.leftAxis.drawAxisLineEnabled = false
        
        
        chartView.drawOrder                 = [DrawOrder.bar.rawValue,  DrawOrder.line.rawValue]
        
        // MARK: xAxis
        let xAxis                           = chartView.xAxis
        xAxis.labelPosition                 = .bothSided
        xAxis.axisMinimum                   = 0.0
        
        xAxis.centerAxisLabelsEnabled = true
        xAxis.setLabelCount( 9, force: true)
        
        // MARK: leftAxis
        let leftAxis                        = chartView.leftAxis
        leftAxis.drawGridLinesEnabled       = false
        leftAxis.drawAxisLineEnabled = false
        leftAxis.axisMinimum                = 0.0
        
    
        
        // MARK: rightAxis
        let rightAxis                       = chartView.rightAxis
        rightAxis.drawGridLinesEnabled      = false
        rightAxis.drawAxisLineEnabled = false
        rightAxis.axisMinimum               = 0.0
        

        
        // MARK: legend
        let legend                          = chartView.legend
        legend.horizontalAlignment          = .center
        legend.verticalAlignment            = .bottom
        legend.orientation                  = .horizontal
        legend.drawInside                   = false
        
        // MARK: description
        chartView.chartDescription?.enabled = false
        
        
        
    }
    
    func setChartData()
    {
        //print("omg i am actually in a different class and this works")
        let data = CombinedChartData()
        data.lineData = generateLineData()
        data.barData = generateBarData()
        chartView.xAxis.axisMaximum = data.xMax + 0.25
        chartView.data = data
    }
    
    func getSubArray(_ array : [Int], _ numElems : Int) -> [Int] {
        var finalArr = [Int](repeating: 0, count: numElems)
        var i = 0
        while i < numElems {
            finalArr[i] = array[i]
            i += 1
        }
        return finalArr
    }
    
    func generateLineData() -> LineChartData
    {
        // MARK: ChartDataEntry
        var entries = [ChartDataEntry]()
        let scoresArray : [Int] = defaults.array(forKey: "scoresArray") as! [Int]
        let n = 9
        let wellBeingValues : [Int] = getSubArray(scoresArray, n)
        
        //var wellBeingValues: [Int]=[3,5,4,4,9,4,7,6,8]
        
        for index in 1..<ITEM_COUNT+1
        {
            entries.append(ChartDataEntry(x: Double(index-1) + 0.5, y: Double(wellBeingValues[index-1])))
        }
        
        // MARK: LineChartDataSet
        let set = LineChartDataSet(values: entries, label: "Well-being Score")
        set.colors = [#colorLiteral(red: 0.941176470588235, green: 0.933333333333333, blue: 0.274509803921569, alpha: 1.0)]
        set.lineWidth = 2.5
        set.circleColors = [#colorLiteral(red: 0.941176470588235, green: 0.933333333333333, blue: 0.274509803921569, alpha: 1.0)]
        set.circleHoleRadius = 2.5
        set.fillColor = #colorLiteral(red: 0.941176470588235, green: 0.933333333333333, blue: 0.274509803921569, alpha: 1.0)
        //set.mode = .cubicBezier
        set.drawValuesEnabled = true
        set.valueFont = NSUIFont.systemFont(ofSize: CGFloat(10.0))
        set.valueTextColor = #colorLiteral(red: 0.941176470588235, green: 0.933333333333333, blue: 0.274509803921569, alpha: 1.0)
        set.axisDependency = .left
        
        // MARK: LineChartData
        let data = LineChartData()
        data.addDataSet(set)
        
        return data
    }
    
    
    func generateBarData() -> BarChartData
    {
        // MARK: BarChartDataEntry
        var entries1 = [BarChartDataEntry]()
        
        let stepsArray : [Int] = defaults.array(forKey: type) as! [Int]
        let n = 9
        let stepsData : [Int] = getSubArray(stepsArray, n)
        print("Steps Data: ")
        print(stepsData)
        
        //var stepsData: [Int] = [500,1000,1234,2000,1500,750,2000,1300,2100]
        
        for index in 1..<ITEM_COUNT+1
        {
            entries1.append(BarChartDataEntry(x: Double(index-1) + 0.5, y: Double(stepsData[index-1])))
            
        }
        var axisLabel : String
        if (type == "callsArray"){
            axisLabel = "Number of calls"
        }
        else{
            axisLabel = "Outdoor steps"
            
        }
        // MARK: BarChartDataSet
       let set1            = BarChartDataSet(values: entries1, label: axisLabel)
        if (type == "callsArray"){
            set1.colors         = [#colorLiteral(red: 0.5208604336, green: 0.5208604336, blue: 0.5208604336, alpha: 1)]
            set1.valueTextColor = #colorLiteral(red: 0.5208604336, green: 0.5208604336, blue: 0.5208604336, alpha: 1)
            
        }
        else{
            set1.colors         = [#colorLiteral(red: 0.1019607843, green: 0.6039215686, blue: 0.662745098, alpha: 1)]
            set1.valueTextColor = #colorLiteral(red: 0.1019607843, green: 0.6039215686, blue: 0.662745098, alpha: 1)
        }
        set1.valueFont      = NSUIFont.systemFont(ofSize: CGFloat(10.0))
        set1.axisDependency = .right
        
        
        
        // MARK: BarChartData
        
        let barWidth = 0.46
        
        // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        let data = BarChartData(dataSet: set1)
        data.barWidth = barWidth
        
        return data
    }
    
    
    
    
    
    
    
}
