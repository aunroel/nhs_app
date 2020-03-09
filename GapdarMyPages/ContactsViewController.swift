//
//  ContactsViewController.swift
//  GapdarMyPages
//
//  Created by localadmin on 09/01/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import UIKit
import ContactsUI

class ContactsViewController: UIViewController, UITextFieldDelegate, CNContactPickerDelegate {
    
    
    let defaults = UserDefaults.standard
    
    var yContactsValue = 10
    var phoneArray : [String] = []
    var nameArray : [String] = []
    
    var phoneNumberTextFields: [UITextField] = []
    var nameTextFields : [UITextField] = []
    
    @IBOutlet weak var contactSV: UIScrollView!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        contactSV.layer.cornerRadius = 15.0
        // Do any additional setup after loading the view, typically from a nib.
        phoneArray = defaults.stringArray(forKey: "phoneArray") ?? []
        nameArray = defaults.stringArray(forKey: "nameArray") ?? []
        setupContactSV()
    }
    
    func setupContactSV() {
        // TODO: Add a for loop to add in the saved contacts.
        view.addSubview(contactSV)
        
        addExistingContacts()
    }
    
    func addExistingContacts() {
        var i = 0
        while i < nameArray.count {
            let name = nameArray[i]
            let phone = phoneArray[i]
            
            let label = UILabel(frame: CGRect(x:10, y:yContactsValue, width:66, height:33))
            label.textAlignment = .left
            label.text = "Name: "
            contactSV.addSubview(label)
            
            let nameTF = UITextField(frame: CGRect(x:74, y:yContactsValue, width:272, height:33))
            nameTF.delegate = self
            nameTF.textAlignment = .center
            nameTF.text = name
            nameTF.backgroundColor = .white
            nameTF.borderStyle = .roundedRect
            contactSV.addSubview(nameTF)
            
            nameTextFields.append(nameTF)
            yContactsValue += 40
            
            let label2 = UILabel(frame: CGRect(x:10, y:yContactsValue, width:166, height:33))
            label2.textAlignment = .left
            label2.text = "Contact Number: "
            contactSV.addSubview(label2)
            
            let numTF = UITextField(frame: CGRect(x:156, y:yContactsValue, width:150, height:33))
            numTF.delegate = self
            numTF.text = phone
            numTF.textAlignment = .center
            numTF.backgroundColor = .white
            numTF.borderStyle = .roundedRect
            contactSV.addSubview(numTF)
            
            // TODO: Add a button to make calls(God DAMMIT)
            let image = UIImage(named: "icons8-call-32.png") as UIImage?
            let button = UIButton.init(type: .roundedRect)
            button.frame = CGRect(x:310, y:yContactsValue, width:32, height:32)
            button.setImage(image, for: .normal)
            button.addTarget(self, action: #selector(callPressed(_ :)), for: .touchUpInside)
            button.tag = i
            contactSV.addSubview(button)
            
            
            phoneNumberTextFields.append(numTF)
            yContactsValue += 40
            
            contactSV.contentSize.height = CGFloat(yContactsValue)
            
            i += 1
        }
        
    }
    
    @objc func callPressed(_ sender: UIButton) {
        // Debug: print(sender.tag)
        let phone = phoneArray[sender.tag]
        let number = String(phone.filter {!" \n\t\r".contains($0)})
        print(number)
        var totalCalls = defaults.integer(forKey: "totalCalls")
        totalCalls += 1
        defaults.set(totalCalls, forKey: "totalCalls")
        print(defaults.integer(forKey: "totalCalls"))
        
        if let url = URL(string: "tel://\(number)"),
            UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url)
        }
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
        let label = UILabel(frame: CGRect(x:44, y:yContactsValue, width:66, height:33))
        //label.center = CGPoint(x:44, y:146)
        label.textAlignment = .center
        label.text = "Name: "
        self.view.addSubview(label)
        
        let namelabel = UITextField(frame: CGRect(x:118, y:yContactsValue, width:272, height:33))
        //namelabel.center = CGPoint(x:118, y:152)
        namelabel.textAlignment = .center
        namelabel.text = familyName
        namelabel.backgroundColor = .white
        self.view.addSubview(namelabel)
        
        yContactsValue += 40
        
        let label2 = UILabel(frame: CGRect(x:44, y:yContactsValue, width:166, height:33))
        //label.center = CGPoint(x:44, y:146)
        label2.textAlignment = .center
        label2.text = "Contact Number: "
        self.view.addSubview(label2)
        
        let numlabel = UITextField(frame: CGRect(x:200, y:yContactsValue, width:150, height:33))
        //namelabel.center = CGPoint(x:118, y:152)
        
        numlabel.text = phoneNumber
        numlabel.backgroundColor = .white
        self.view.addSubview(numlabel)
        
        yContactsValue += 40
    }
    
    func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        print("It cancelled the contact picker view controller when the cancel button is pressed")
    }

    @IBAction func resetStart(_ sender: Any) {
        defaults.set(false, forKey: "setup")
    }
    
}
