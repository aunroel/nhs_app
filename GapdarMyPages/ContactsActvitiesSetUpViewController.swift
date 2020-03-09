//
//  ContactsViewController.swift
//  GapdarMyPages
//
//  Created by localadmin on 07/12/2019.
//  Copyright © 2019 localadmin. All rights reserved.
//

import UIKit
import ContactsUI

class ContactsActvitiesSetUpViewController: UIViewController, UITextFieldDelegate, CNContactPickerDelegate {
    
    var yContactsValue = 10
    var yActivityValue = 10
    
    
    var phoneNumberTextFields: [UITextField] = []
    var nameTextFields : [UITextField] = []
    var activityTextFields : [UITextField] = []
    
    
    var phoneNumberString : [String] = []
    var nameString : [String] = []
    var activityString : [String] = []
    
    @IBOutlet weak var contactLabel: UILabel!
    @IBOutlet weak var contactButton: UIButton!
    @IBOutlet weak var activityLabel: UILabel!
    @IBOutlet weak var activityButton: UIButton!
    
    let defaults = UserDefaults.standard
    
    @IBOutlet weak var contactScrollView: UIScrollView!
    
    @IBOutlet weak var activityScrollView: UIScrollView!
    
    
    
//    lazy var stackView: UIStackView = {
//        let sv = UIStackView(arrangedSubviews: [contactLabel, contactScrollView, contactButton, activityLabel, activityScrollView, activityButton])
//        sv.translatesAutoresizingMaskIntoConstraints = false
//        sv.axis = .vertical
//        sv.spacing = 20
//        sv.alignment = .leading
//        sv.distribution = .equalSpacing
//        return sv
//    }()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        contactScrollView.layer.cornerRadius = 10.0
        activityScrollView.layer.cornerRadius = 10.0
        // Do any additional setup after loading the view, typically from a nib.
    }
    

  
    
    @IBAction func getNumber(_ sender: Any) {
        let picker = CNContactPickerViewController()
        picker.delegate = self
        present(picker, animated: true, completion: nil)
        
    }
   
    
    func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        var familyName = ""
        var phoneNumber = ""
        for data in contact.phoneNumbers{
            let phoneNo = data.value
            familyName = contact.givenName + " "+contact.familyName
            phoneNumber = phoneNo.stringValue

        }
        
        let label = UILabel(frame: CGRect(x:10, y:yContactsValue, width:66, height:33))
        label.textAlignment = .left
        label.text = "Name: "
        contactScrollView.addSubview(label)
        
        let nameTF = UITextField(frame: CGRect(x:74, y:yContactsValue, width:272, height:33))
        nameTF.delegate = self
        nameTF.textAlignment = .center
        nameTF.text = familyName
        nameTF.backgroundColor = .white
        nameTF.borderStyle = .roundedRect
        contactScrollView.addSubview(nameTF)

        nameTextFields.append(nameTF)
        yContactsValue += 40
        
        let label2 = UILabel(frame: CGRect(x:10, y:yContactsValue, width:166, height:33))
        label2.textAlignment = .left
        label2.text = "Contact Number: "
        contactScrollView.addSubview(label2)
        
        let numTF = UITextField(frame: CGRect(x:156, y:yContactsValue, width:150, height:33))
        numTF.delegate = self
        numTF.text = phoneNumber
        numTF.textAlignment = .center
        numTF.backgroundColor = .white
        numTF.borderStyle = .roundedRect
        contactScrollView.addSubview(numTF)
        phoneNumberTextFields.append(numTF)
        yContactsValue += 40
        
        contactScrollView.contentSize.height = CGFloat(yContactsValue)
    }

    func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        print("It cancelled the contact picker view controller when the cancel button is pressed")
    }
    
    
    
    
    
    @IBAction func activityButtonPressed(_ sender: Any) {
        let label = UILabel(frame: CGRect(x:10, y:yActivityValue, width:74, height:33))
        //label.center = CGPoint(x:44, y:146)
        label.textAlignment = .left
        label.text = "Activity: "
        activityScrollView.addSubview(label)
        
        let activityTF = UITextField(frame: CGRect(x:80, y:yActivityValue, width:231, height:33))
        activityTF.delegate = self
        activityTF.backgroundColor = .white
        activityTF.textAlignment = .left
        activityTF.borderStyle = .roundedRect
        
        activityScrollView.addSubview(activityTF)
        activityTextFields.append(activityTF)
        yActivityValue += 40
        
        activityScrollView.contentSize.height = CGFloat(yActivityValue)
        
    }
    
    

    @IBAction func saveButtonPressed(_ sender: Any) {
        //print("I am in here")
        for phoneNumber in phoneNumberTextFields{
            phoneNumberString.append(phoneNumber.text ?? "-")
        }
        
        for name in nameTextFields{
            nameString.append(name.text ?? "-")
        }
        
        for activity in activityTextFields{
            activityString.append(activity.text ?? "-")
        }
        defaults.set(phoneNumberString, forKey: "phoneArray")
        defaults.set(nameString, forKey: "nameArray")
        defaults.set(activityString, forKey: "activityArray")
        
        /*
        debug use only:
        print(defaults.stringArray(forKey: "phoneArray")!)
        print(defaults.stringArray(forKey: "nameArray")!)
        print(defaults.stringArray(forKey: "activityArray")!)
        
        print(phoneNumberString)
        print(nameString)
        print(activityString)
         */
        self.performSegue(withIdentifier: "startTrackingSegue", sender: self)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        // Hide the keyboard
        textField.resignFirstResponder()
        return true
    }
    
  
}







