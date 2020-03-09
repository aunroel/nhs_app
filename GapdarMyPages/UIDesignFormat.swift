//
//  UIDesignFormat.swift
//  GapdarMyPages
//
//  Created by localadmin on 29/01/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import UIKit

extension UIView{
    
    func setGradientBackground(){
        let newLayer = CAGradientLayer()
        newLayer.frame = bounds
        newLayer.colors = [UIColor(red: 164/255.0, green: 200/255.0, blue: 255/255.0, alpha: 1.0).cgColor, UIColor(red: 17/255.0, green: 40/255.0, blue: 123/255.0, alpha: 1.0).cgColor]
        layer.insertSublayer(newLayer, at: 0)    }
}
