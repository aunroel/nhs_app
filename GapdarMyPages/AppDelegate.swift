//
//  AppDelegate.swift
//  GapdarMyPages
//
//  Created by localadmin on 07/12/2019.
//  Copyright © 2019 localadmin. All rights reserved.
//

import UIKit
import UserNotifications
import Firebase

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    var window: UIWindow?
    
    let defaults = UserDefaults.standard


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        UNUserNotificationCenter.current().delegate = self
        //let someDelegate = stepController()
        //UNUserNotificationCenter.current().delegate = someDelegate
        
        //Setting the First View Controller
        self.window = UIWindow(frame: UIScreen.main.bounds)
        
        let mainSB : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let barSB : UIStoryboard = UIStoryboard(name: "MenuTabBar", bundle: nil)
        
        if (defaults.bool(forKey: "setup") == false) {
            let initialVC = mainSB.instantiateViewController(withIdentifier: "Setup")
            self.window?.rootViewController = initialVC
        } else {
            let barVC = barSB.instantiateViewController(withIdentifier: "tabBar")
            self.window?.rootViewController = barVC
        }
        
        self.window?.makeKeyAndVisible()
        
        //Setting Tab Appearance
        
        // Override point for customization after application launch.
        
        //Background Fetch as quickly as possible (3600 for once an hour)
        UIApplication.shared.setMinimumBackgroundFetchInterval(UIApplication.backgroundFetchIntervalMinimum)
        return true
    }
    
    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        let vc = stepController()
        vc.nudge()
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        //This is the method called if notification is received while app is in foreground.
        print("I've reached willPresent method")
        
        let identifier = notification.request.identifier
        if identifier == "weekly" {
            presentWBAlert()
        }
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        print("I did receive a response")
        
        //Debug: This prints the notification that was clicked
        //print(response.notification.request.identifier)
        
        let identifier = response.notification.request.identifier
        if identifier == "weekly" {
            presentWBAlert()
        }
        
        completionHandler()
    }
    
    func getPredictedScore() -> Int {
        let VC = stepController()
        VC.getThisWeekSteps()
        
        //Unfortunately there is potential that the steps isn't called in time based on current understanding.
        let weekSteps = defaults.integer(forKey: "oneWeekSteps")
        print("The weekSteps appears to be: " + String(weekSteps))
        let targetSteps = Int(defaults.string(forKey: "targetSteps") ?? "1000")!
        let weekCalls = defaults.integer(forKey: "totalCalls")
        let targetCalls = Int(defaults.string(forKey: "targetCalls") ?? "3")!
        
        let avgSteps = weekSteps/7
        let avgCalls = weekCalls/7
        
        var stepsRatio : Double = Double(avgSteps)/Double(targetSteps)
        if stepsRatio > 1 {
            stepsRatio = 1.0
        }
        
        var callsRatio : Double = Double(avgCalls)/Double(targetCalls)
        if callsRatio > 1 {
            callsRatio = 1.0
        }
        
        let score : Double = (stepsRatio + callsRatio) / 2.0 * 10.0
        
        //Record down the predicted score, if modified will update later.
        defaults.set(score, forKey: "score")
        
        //This function shifts the records or steps and calls to put current week into last week
        updateArrays()
        
        if Int(score) > 10 {
            return 10
        }
        
        return Int(score)
    }
    
    func updateArrays() {
        // Structure: Index 0 corresponds to last week, Index 11 corresponds to 12 weeks ago
        var callsArray : [Int] = defaults.array(forKey: "callsArray") as! [Int]
        var stepsArray : [Int] = defaults.array(forKey: "stepsArray") as! [Int]
        var scoresArray : [Int] = defaults.array(forKey: "scoresArray") as! [Int]
        var i = 11;
        while i > 0 {
            callsArray[i] = callsArray[i-1]
            stepsArray[i] = stepsArray[i-1]
            scoresArray[i] = scoresArray[i-1]
            i = i-1
        }
        //Set last week's steps and calls
        callsArray[0] = defaults.integer(forKey: "totalCalls")
        stepsArray[0] = defaults.integer(forKey: "oneWeekSteps")
        scoresArray[0] = defaults.integer(forKey: "score")
        
        //Store the arrays:
        defaults.set(callsArray, forKey: "callsArray")
        defaults.set(stepsArray, forKey: "stepsArray")
        defaults.set(scoresArray, forKey: "scoresArray")
        print(callsArray)
        print(stepsArray)
        print(scoresArray)
        
        //Clear the current accumulations for steps and calls, not scores because that remains
        defaults.set(0, forKey: "oneWeekSteps")
        defaults.set(0, forKey: "totalCalls")
    }
    
    func presentWBAlert() {
        //Call a function to calculate predicted score here
        let predicted = getPredictedScore()
        let userMessage = "Do you think this is accurate?"
        let myAlert = UIAlertController(title: "Predicted Score: \(predicted)", message: userMessage, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "Yes", style: UIAlertAction.Style.default){
            UIAlertAction in
            print("I pressed Yes to alert")
            
//            //Set predicted into model
//            self.defaults.set(predicted, forKey: "score")
            
            //Navigate to Home page
            let barSB : UIStoryboard = UIStoryboard(name: "MenuTabBar", bundle: nil)
            let VC = barSB.instantiateViewController(withIdentifier: "tabBar")
            self.window?.rootViewController = VC
            self.window?.makeKeyAndVisible()
        }
        let NoAction = UIAlertAction(title: "No", style: UIAlertAction.Style.default){
            UIAlertAction in
            print("I pressed No to alert")
            
            //Navigate to the adjustment page
            let homeSB : UIStoryboard = UIStoryboard(name: "Home", bundle: nil)
            let VC = homeSB.instantiateViewController(withIdentifier: "adjust")
            self.window?.rootViewController = VC
            self.window?.makeKeyAndVisible()
        }
        myAlert.addAction(okAction)
        myAlert.addAction(NoAction)
        self.window?.rootViewController?.present(myAlert, animated: true, completion: nil)
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        application.applicationIconBadgeNumber = 0
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    func applicationDidFinishLaunching(_ application: UIApplication) {
        FirebaseApp.configure()
    }


}

