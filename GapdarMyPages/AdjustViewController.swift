//
//  AdjustViewController.swift
//  GapdarMyPages
//
//  Created by Paul Lam on 29/1/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit

class AdjustViewController: UIViewController {
    
    let defaults = UserDefaults.standard
    
    @IBOutlet weak var scoreLabel: UILabel!
    
    @IBAction func slider(_ sender: UISlider) {
        scoreLabel.text = String(Int(sender.value))
        
    }

    @IBOutlet weak var sliderView: UIView!
    
    
    @IBOutlet weak var wellBeingLabel: UILabel!
    
    @IBOutlet weak var figuresView: UIView!
    
    
    @IBOutlet weak var saveButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        sliderView.layer.masksToBounds = true
        sliderView.layer.cornerRadius = 10.0
        wellBeingLabel.layer.masksToBounds = true
        wellBeingLabel.layer.cornerRadius = 10.0
        figuresView.layer.masksToBounds = true
        figuresView.layer.cornerRadius = 10.0
        
        

        // Do any additional setup after loading the view.
    }
    
    @IBAction func savePressed(_ sender: Any) {
        let predictedScore : Int = Int(UserDefaults.standard.string(forKey: "score")!)!
        let actualScore : Int = Int(scoreLabel.text!)!
        let errorRate : Int = actualScore - predictedScore
        
        defaults.set(errorRate, forKey: "errorRate")
        defaults.set(actualScore, forKey: "score")
        
        //Update the scoresArray with this score.
        var scoresArray : [Int] = defaults.array(forKey: "scoresArray") as! [Int]
        scoresArray[0] = defaults.integer(forKey: "score")
        defaults.set(scoresArray, forKey: "scoresArray")
        
        let barSB : UIStoryboard = UIStoryboard(name: "MenuTabBar", bundle: nil)
        let barVC = barSB.instantiateViewController(withIdentifier: "tabBar")
        let appDelegate = UIApplication.shared.delegate
        appDelegate?.window??.rootViewController = barVC
        appDelegate?.window??.makeKeyAndVisible()
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
