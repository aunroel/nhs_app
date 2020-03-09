//
//  connectToCareNetwork.swift
//  GapdarMyPages
//
//  Created by localadmin on 30/01/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import UIKit

class ConnectToCareNetwork: UIViewController {
    
    @IBOutlet weak var viewTickCross: UIScrollView!
   
    override func viewDidLoad() {
        super.viewDidLoad()
        viewTickCross.layer.cornerRadius = 10.0
        
        viewTickCross.backgroundColor = .white
        // Do any additional setup after loading the view, typically from a nib.
    }
}
