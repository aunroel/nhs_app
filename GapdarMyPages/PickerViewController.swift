//
//  PickerViewController.swift
//  AlertApp
//
//  Created by Paul Lam on 9/12/2019.
//  Copyright © 2019 Team33. All rights reserved.
//

import UIKit
import MessageUI

class PickerViewController: UIViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource {

    
    @IBOutlet weak var nameTextfield: UITextField!
    @IBOutlet weak var namePicker: UIPickerView!
    
    var namePickerData : [String] = [String]()
    
    var selection : String?
    
    let defaults = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        namePicker.isHidden = true
        self.namePicker.delegate = self
        self.namePicker.dataSource = self
        nameTextfield.delegate = self
        namePickerData = ["Paul", "Karunya", "Lishen", "Joseph"]
    }
    
    // MARK: UIPickerViewDelegate
    
    //Number of Columns of Data
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // Number of Rows of Data
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return namePickerData.count
    }
    
    // Data to return for the row and component (column) being passed in
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return namePickerData[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selection = namePickerData[row]
        nameTextfield.text = selection
        defaults.set(selection, forKey: "selectedPerson")
        namePicker.isHidden = true
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        namePicker.isHidden = false
        return false
    }
    
    

}
