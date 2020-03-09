//
//  rotateLabel.swift
//  GapdarMyPages
//
//  Created by localadmin on 06/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit


@IBDesignable
class rotateLabel: UILabel{
    @IBInspectable var rotation: CGFloat = 0{
        didSet{
            let radians = CGFloat(CGFloat(Double.pi)*CGFloat(rotation)/CGFloat(180.0))
            self.transform = CGAffineTransform(rotationAngle: radians)
        }
    }
    
    
    
}
