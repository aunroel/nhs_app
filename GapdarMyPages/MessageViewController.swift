//
//  MessageViewController.swift
//  AlertApp
//
//  Created by Paul Lam on 21/11/2019.
//  Copyright © 2019 Team33. All rights reserved.
//

import UIKit
import MessageUI
import UserNotifications

class MessageViewController: UIViewController, UITextFieldDelegate, MFMessageComposeViewControllerDelegate  {
    //MARK: Properties
    @IBOutlet weak var messageTextField: UITextField!
    @IBOutlet weak var messageLabel: UILabel!
    @IBOutlet weak var personLabel: UILabel!
    
    @IBOutlet weak var preComposedButton: UIButton!
    var msg: String?
    
    let defaults = UserDefaults.standard
        
    
    override func viewDidLoad() {
        super.viewDidLoad()
        messageTextField.layer.masksToBounds = true
        messageTextField.layer.cornerRadius = 10.0
        //Handle the text field's user input through delegate callback
        messageTextField.delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge], completionHandler: {didAllow, error in})
    }
    
    //MARK: UITextFieldDelegate
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        // Hide the keyboard
        textField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        messageLabel.text = textField.text
        msg = textField.text! //Unwraps the optional
    }
    
    
    //MARK: Actions
    @IBAction func sendMessage(_ sender: UIButton) {
        if MFMessageComposeViewController.canSendText()
        {
            let msgVC = MFMessageComposeViewController()
            msgVC.body = msg ?? "Example Message Body"
            msgVC.recipients = ["07460068823"]
            msgVC.messageComposeDelegate = self
            self.present(msgVC, animated: true, completion: nil)
        }
    }
    
    func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func updatePressed(_ sender: UIButton) {
        personLabel.text = defaults.string(forKey: "selectedPerson")
    }
    
    @IBAction func notificationPressed(_ sender: Any) {
        let content = UNMutableNotificationContent()
        content.title = "The 5 seconds are up!"
        content.subtitle = "They are up now!"
        content.body = "The 5 seconds are really up!"
        content.badge = 1
        
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)
        
        let request = UNNotificationRequest(identifier: "timerDone", content: content, trigger: trigger)
        
        UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
    }
    
}
