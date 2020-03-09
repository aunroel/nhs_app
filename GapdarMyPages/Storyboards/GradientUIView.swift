//
//  GradientUIView.swift
//  GapdarMyPages
//
//  Created by localadmin on 02/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit

@IBDesignable
class GradientUIView:UIView {
    
    @IBInspectable public var firstColor: UIColor = UIColor.white{
        didSet {
            updateView()
        }
    }
    
    @IBInspectable public var secondColor: UIColor = UIColor.white{
        didSet {
            updateView()
        }
    }
    
    override class var layerClass: AnyClass{
        get{
            return CAGradientLayer.self
        }
    }
    
    func updateView(){
        let layer = self.layer as! CAGradientLayer
        layer.colors = [firstColor.cgColor,  secondColor.cgColor]
        layer.startPoint = CGPoint(x:0.0, y:0.0)
        layer.endPoint = CGPoint(x:0.0, y:1.0)
    }
    
}
