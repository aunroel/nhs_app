//
//  StatisticsViewController.swift
//  GapdarMyPages
//
//  Created by localadmin on 28/12/2019.
//  Copyright © 2019 localadmin. All rights reserved.
//

import UIKit
import Charts
import Foundation

class CallsStatisticsViewController: UIViewController{

   
    @IBOutlet weak var titleLabel: UILabel!
    

    
    @IBOutlet weak var graphTitleLabel: UILabel!
    let defaults = UserDefaults.standard
    
    @IBOutlet weak var stepButton: UIButton!
    @IBOutlet weak var callButton: UIButton!
    //@IBOutlet weak var chartView: CombinedChartView!
    @IBOutlet weak var chartView: CombinedChartView!
    //var stepsTakenGraph: UIView!
    //var wellBeingScoreGraph: UIView!
    let weeks = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]
    let ITEM_COUNT = 9
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
       
        //self.navigationItem.hidesBackButton = true
        Graph(chartView: chartView, type: "callsArray").setChartData()
        chartView.backgroundColor = .white
        chartView.layer.cornerRadius = 10.0
        chartView.clipsToBounds = true
        
        
    }

    
    
    @IBAction func outdoorButtonPressed(_ sender: Any) {
        titleLabel.text = "Steps vs Well-being"
        graphTitleLabel.text = "Outdoor steps and Well-being"
        Graph(chartView: chartView, type: "stepsArray").setChartData()
    }
    @IBAction func callsButtonPressed(_ sender: Any) {
        titleLabel.text = "Calls vs Well-being"
        graphTitleLabel.text = "Calls made and Well-being"
        Graph(chartView: chartView, type: "callsArray").setChartData()
    }
}

