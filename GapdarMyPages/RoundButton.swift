//
//  RoundButton.swift
//  GapdarMyPages
//
//  Created by localadmin on 02/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import  UIKit

@IBDesignable
class RoundButton: UIButton{
    @IBInspectable var cornerRadius: CGFloat = 0{
        didSet{
            self.layer.cornerRadius = cornerRadius
        }
    }
    
    
    
}
