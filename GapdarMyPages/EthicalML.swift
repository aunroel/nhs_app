//
//  EthicalML.swift
//  GapdarMyPages
//
//  Created by Davinder Bassan on 21/02/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import SwiftyJSON
import TensorFlowLite

public class EthicalML {
    
    @objc public static func uploadDataToServer(postcode:String, wellBeingScore:String, weeklySteps:String, weeklyCalls:String, errorRate:String, supportCode:String){
        //declare parameter as a dictionary which contains string as key and value combination. considering inputs are valid

        let parameters: [String: String] = [
            "Postcode":postcode,
            "WellBeingScore":wellBeingScore,
            "WeeklySteps":weeklySteps,
            "WeeklyCalls":weeklyCalls,
            "ErrorRate":errorRate,
            "SupportCode":supportCode]

        //create the url with URL
        let url = URL(string: "http://myServerName.com/api")! //change the url

        //create the session object
        let session = URLSession.shared

        //now create the URLRequest object using the url object
        var request = URLRequest(url: url)
        request.httpMethod = "POST" //set http method as POST

        do {
            request.httpBody = try JSONSerialization.data(withJSONObject: parameters, options: .prettyPrinted) // pass dictionary to nsdata object and set it as request body

        } catch let error {
            print(error.localizedDescription)
        }
        
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")

        //create dataTask using the session object to send data to the server
        let task = session.dataTask(with: request, completionHandler: { data, response, error in

            guard error == nil else {
                return
            }

            guard let data = data else {
                return
            }

            do {
                //create json object from data
                if let json = try JSONSerialization.jsonObject(with: data, options: .mutableContainers) as? [String: Any] {
                    print(json)
                    // handle json...
                }

            } catch let error {
                print(error.localizedDescription)
            }
        })
        task.resume()
    }
    
    public static func downloadModelFromServer(){
        
    }
    
    public static func predictionUsingModel(){
        
    }
    
    public static func repeatSchedulerEvery7Days(){
        //create a date component for 02/02/20 23:59 (a Sunday night)
        //we use this date to give the timer a starting point
        //then we say repeat this every 604800 seconds
        
        var dateComponents = DateComponents()
        dateComponents.year = 2020
        dateComponents.month = 2
        dateComponents.day = 2
        dateComponents.minute = 59
        dateComponents.hour = 23
        dateComponents.timeZone = TimeZone(abbreviation: "GMT")
        let userCalendar = Calendar.current
        let startDate = userCalendar.date(from: dateComponents)!
//        let date = (startDate?.addingTimeInterval(500))! //repeats every 7 days 604800
        let timer = Timer(fireAt: startDate, interval: 10, target: self, selector: #selector(uploadDataToServer), userInfo: nil, repeats: true)
        //the selector in the line above specifies which function to run
        RunLoop.main.add(timer, forMode: .common) //the
        
    }
    
    public static func someBackgroundTask(timer:Timer) {
        DispatchQueue.global(qos: DispatchQoS.background.qosClass).async {
            print("do some background task")
//            uploadDataToServer()
            

            DispatchQueue.main.async {
                print("update some UI")
            }
        }
    }
  
}
