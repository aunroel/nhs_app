//
//  ContactHistory.swift
//  GapdarMyPages
//
//  Created by localadmin on 06/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import UIKit

class ContactHistory:UIViewController{
    
    @IBOutlet weak var historyScroller: UIScrollView!
    
    override func viewDidLoad() {
        historyScroller.layer.masksToBounds = true
        historyScroller.layer.cornerRadius = 10.0
        placeInformation()
        
        
        
    }
    
    func placeInformation(){
        var yposition = 60
        for x in 1...5{
            print(x)
            
            
            
            let nameLabel = UILabel(frame:CGRect(x:44, y:yposition, width: 66, height:33))
            nameLabel.textAlignment = .left
            nameLabel.text = "Name: "
            historyScroller.addSubview(nameLabel)
            
            let callLabel = UILabel(frame:CGRect(x:274, y:yposition, width: 50, height:33))
            
            callLabel.textAlignment = .left
            callLabel.text = "0"
            historyScroller.addSubview(callLabel)
            
            
            let messageLabel = UILabel(frame:CGRect(x:210, y:yposition, width: 50, height:33))
            messageLabel.textAlignment = .left
            messageLabel.text = "0"
            historyScroller.addSubview(messageLabel)
            
            yposition += 40
        }
        
        
        
        
        //        let label = UILabel(frame: CGRect(x:44, y:yContactsValue, width:66, height:33))
//        //label.center = CGPoint(x:44, y:146)
//        label.textAlignment = .center
//        label.text = "Name: "
//        self.view.addSubview(label)
//
//        let namelabel = UITextField(frame: CGRect(x:118, y:yContactsValue, width:272, height:33))
//        //namelabel.center = CGPoint(x:118, y:152)
//        namelabel.textAlignment = .center
//        namelabel.text = familyName
//        namelabel.backgroundColor = .white
//        self.view.addSubview(namelabel)
//
//        yContactsValue += 40
//
//        let label2 = UILabel(frame: CGRect(x:44, y:yContactsValue, width:166, height:33))
//        //label.center = CGPoint(x:44, y:146)
//        label2.textAlignment = .center
//        label2.text = "Contact Number: "
//        self.view.addSubview(label2)
//
//        let numlabel = UITextField(frame: CGRect(x:200, y:yContactsValue, width:150, height:33))
//        //namelabel.center = CGPoint(x:118, y:152)
//
//        numlabel.text = phoneNumber
//        numlabel.backgroundColor = .white
//        self.view.addSubview(numlabel)
//
//        yContactsValue += 40
//    
    }
    
}
