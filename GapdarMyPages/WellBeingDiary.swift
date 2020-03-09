//
//  WellBeingDiary.swift
//  GapdarMyPages
//
//  Created by localadmin on 04/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import UIKit
import Charts

class WellBeingDiary:UIViewController, UIGestureRecognizerDelegate{

    @IBOutlet weak var callsChartView: CombinedChartView!
    
    

    @IBOutlet weak var stepsChartView: CombinedChartView!
    

    
    override func viewDidLoad() {
        super.viewDidLoad()
        callsChartView.isUserInteractionEnabled = true
        let gesture = UITapGestureRecognizer(target:self, action: #selector(self.callCallsPage))
        gesture.delegate = self
        callsChartView.addGestureRecognizer(gesture)
        gesture.numberOfTapsRequired = 1
        stepsChartView.isUserInteractionEnabled = true
        let stepGesture = UITapGestureRecognizer(target:self, action: #selector(self.callStepsPage))
        stepGesture.delegate = self
        stepsChartView.addGestureRecognizer(stepGesture)
        stepGesture.numberOfTapsRequired = 1
        Graph(chartView: callsChartView, type: "callsArray").setChartData()
        callsChartView.layer.cornerRadius = 10.0
        callsChartView.backgroundColor = .white
        callsChartView.clipsToBounds = true
        
        Graph(chartView: stepsChartView, type: "stepsArray").setChartData()
        stepsChartView.layer.cornerRadius = 10.0
        stepsChartView.backgroundColor = .white
        stepsChartView.clipsToBounds = true
        
    }
    
    @objc func callCallsPage(_sender:UITapGestureRecognizer){
        print("it is being clicked")
        self.performSegue(withIdentifier: "callGraph", sender: self)
    }
    
    @objc func callStepsPage(_sender:UITapGestureRecognizer){
        print("it is being clicked")
        self.performSegue(withIdentifier: "stepsCall", sender: self)
    }
}
