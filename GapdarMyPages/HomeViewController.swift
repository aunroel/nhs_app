//
//  ViewController.swift
//  GapdarMyPages
//
//  Created by localadmin on 07/12/2019.
//  Copyright © 2019 localadmin. All rights reserved.
//

import UIKit

class HomeViewController: UIViewController {
    var timer = Timer.scheduledTimer(withTimeInterval: 5.0, repeats: true) {
      timer in

      EthicalML.someBackgroundTask(timer: timer)
    }
    @IBOutlet weak var scoreView: UIView!
    //    let label = UILabel()
    
    @IBOutlet weak var figuresView: UIView!
    
    let defaults = UserDefaults.standard
    
    @IBOutlet weak var displayedScoreLabel: UILabel!
    
    @IBOutlet weak var wellBeingLabel: UILabel!
    
    @IBOutlet weak var backgroundLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
//        EthicalML.repeatSchedulerEvery7Days()
        scoreView.layer.masksToBounds = true
        scoreView.layer.cornerRadius = 10.0
        wellBeingLabel.layer.cornerRadius = 10.0
        wellBeingLabel.layer.masksToBounds = true
        figuresView.layer.masksToBounds = true
        figuresView.layer.cornerRadius = 10.0
                backgroundLabel.layer.cornerRadius = backgroundLabel.layer.bounds.height / 2
                backgroundLabel.layer.masksToBounds = true
                let newLayer = CAGradientLayer()
                newLayer.frame = backgroundLabel.bounds
                newLayer.colors = [UIColor(red: 164/255.0, green: 200/255.0, blue: 255/255.0, alpha: 1.0).cgColor, UIColor(red: 17/255.0, green: 40/255.0, blue: 123/255.0, alpha: 1.0).cgColor]
                newLayer.locations = [0.0, 1.0]
                newLayer.startPoint = CGPoint(x:1.0, y:0.0)
                newLayer.endPoint = CGPoint(x:0.0, y:0.0)
                backgroundLabel.layer.addSublayer(newLayer)        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        

        displayedScoreLabel.text = String(defaults.integer(forKey: "score"))
    }
    
}

