//
//  PermissionsViewController.swift
//  GapdarMyPages
//
//  Created by Paul Lam on 29/1/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit

class PermissionsViewController: UIViewController {
    
    let defaults = UserDefaults.standard

    @IBOutlet weak var startTracking: UIButton!
    
    @IBOutlet weak var viewTracking: UIView!
    
    
    @IBOutlet weak var viewScoreShare: UIView!
    
    @IBOutlet weak var sharingSwitch: UISwitch!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.setGradientBackground()
        viewTracking.layer.cornerRadius = 15.0
        viewScoreShare.layer.cornerRadius = 15.0
        viewScoreShare.layer.borderWidth = 5.0
        viewScoreShare.layer.borderColor = UIColor.white.cgColor
        
        startTracking.layer.masksToBounds = true
        let newLayer = CAGradientLayer()
        newLayer.frame = startTracking.bounds
         newLayer.colors = [UIColor(red: 164/255.0, green: 200/255.0, blue: 255/255.0, alpha: 1.0).cgColor, UIColor(red: 17/255.0, green: 40/255.0, blue: 123/255.0, alpha: 1.0).cgColor]
        newLayer.locations = [0.0, 1.0]
        newLayer.startPoint = CGPoint(x:1.0, y:0.0)
        newLayer.endPoint = CGPoint(x:0.0, y:0.0)
        startTracking.layer.insertSublayer(newLayer, at: 0)
        
            // Do any additional setup after loading the view.
    }
    
    
    
    @IBAction func trackingPressed(_ sender: Any) {
        setupHistory()
        let setup = true
        defaults.set(setup, forKey: "setup")
        
        let homeSB : UIStoryboard = UIStoryboard(name: "Home", bundle: nil)
        let VC = homeSB.instantiateViewController(withIdentifier: "adjust")
        let appDelegate = UIApplication.shared.delegate
        appDelegate?.window??.rootViewController = VC
        appDelegate?.window??.makeKeyAndVisible()
        
    }
    
    @IBAction func sharingSwitchPressed(_ sender: Any) {
        defaults.set(sharingSwitch.isOn, forKey: "allowShare")
        //Debugging
        //print(defaults.bool(forKey: "allowShare"))
        
    }
    
    func setupHistory() {
        if defaults.array(forKey: "callsArray") == nil {
            let callsArray = [Int](repeating: 0, count: 12)
            defaults.set(callsArray, forKey: "callsArray")
            print(callsArray)
        }
        if defaults.array(forKey: "stepsArray") == nil {
            let stepsArray = [Int](repeating: 0, count: 12)
            defaults.set(stepsArray, forKey: "stepsArray")
            print(stepsArray)
        }
        if defaults.array(forKey: "scoresArray") == nil {
            let scoresArray = [Int](repeating: 0, count: 12)
            defaults.set(scoresArray, forKey: "scoresArray")
            print(scoresArray)
        }
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
