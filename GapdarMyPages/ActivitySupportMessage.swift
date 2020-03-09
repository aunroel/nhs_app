//
//  ActivitySupportMessage.swift
//  GapdarMyPages
//
//  Created by localadmin on 06/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit

class ActivitySupportMessage:UIViewController {
    
    @IBOutlet weak var messageView: UIView!
    override func viewDidLoad() {
        messageView.layer.masksToBounds = true
        messageView.layer.cornerRadius = 10
        
    }

}
