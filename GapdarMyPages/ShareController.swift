//
//  Share	Controller.swift
//  pdf
//
//  Created by Mac Mini on 12/26/19.
//  Copyright © 2019 Mac Mini. All rights reserved.
//


import UIKit
import PDFKit

class ShareController: UIViewController, UIDocumentInteractionControllerDelegate{
    let defaults = UserDefaults.standard
    let date = Date()   

    @IBOutlet weak var btCreatePDF: UIButton!
    @IBOutlet weak var btViewPDF: UIButton!
    @IBOutlet weak var btSharePDF: UIButton!
    var filePath: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    
    @IBAction func createPDF(_ sender: Any) {
        messageCreatedPDF()
        
    }
    func create(){
        let documentsDirectory = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
        filePath = (documentsDirectory as NSString).appendingPathComponent("Test.pdf") as String
        
        let format = DateFormatter()
        format.dateFormat = "yyyy-MM-dd"
        let formattedDate = format.string(from: date)
        defaults.set(formattedDate, forKey: "date")
        
        let pdfTitle = "Summary of Your Scores"
        
        let pdfSubject = "Dates:"
        let pdfText = defaults.value(forKey: "date") as? String ?? ""
        let pdfSubject2 = "NHS Number:"
        let pdfText2 = defaults.value(forKey: "refer1") as? String ?? ""
        let pdfSubject3 = "EMIS Number:"
        let pdfText3 = defaults.value(forKey: "refer2") as? String ?? ""
        let pdfSubject4 = "Postcode:"
        let pdfText4 = defaults.value(forKey: "postcode") as? String ?? ""
        let pdfSubject5 = "Predicted Score:"
        let pdfText5 = "\(defaults.integer(forKey: "lastscore"))"
        let pdfSubject6 = "Score Input By User:"
        let pdfText6 = "\(defaults.integer(forKey: "New score"))"
        print(pdfText6)
        let pdfMetadata = [
            // The name of the application.
            kCGPDFContextCreator: "WellWellWell"
            
            // etc.
        ]
        
        // Creates a new PDF file at path.
        UIGraphicsBeginPDFContextToFile(filePath, CGRect.zero, pdfMetadata)
        
        // Creates a new page in the current PDF context.
        UIGraphicsBeginPDFPage()
        
        // PDF default: width 612.0, height 792.0
        let pageSize = UIGraphicsGetPDFContextBounds().size
        // Custom fonts
        let fontTitle = UIFont.preferredFont(forTextStyle: .largeTitle)
        
        let fontSubject = UIFont.preferredFont(forTextStyle: .headline)
        let fontText = UIFont.preferredFont(forTextStyle: .body)
        
        // Drawing the title of the PDF on top of the page.
        let attributedPDFTitle = NSAttributedString(string: pdfTitle, attributes: [NSAttributedString.Key.font: fontTitle])
        let stringSize = attributedPDFTitle.size()
        let stringRect = CGRect(x: (pageSize.width / 2 - stringSize.width / 2), y: 30, width: stringSize.width, height: stringSize.height)
        attributedPDFTitle.draw(in: stringRect)
        
        // Drawing the "Date:" below
        let attributedPDFSubject = NSAttributedString(string: pdfSubject, attributes: [NSAttributedString.Key.font: fontSubject])
        let stringSizeSubject = attributedPDFSubject.size()
        let stringRectSubject = CGRect(x: 60, y: 100, width: stringSizeSubject.width, height: stringSizeSubject.height)
        attributedPDFSubject.draw(in: stringRectSubject)
        // Drawing dates:
        let attributedPDFText = NSAttributedString(string: pdfText, attributes: [NSAttributedString.Key.font: fontText])
        let stringSizeText = attributedPDFText.size()
        let stringRectText1 = CGRect(x: 60, y: 140, width: stringSizeText.width, height: stringSizeText.height)
        attributedPDFText.draw(in: stringRectText1)
        
        // Drawing the "Reference 1" below
        let attributedPDFSubject2 = NSAttributedString(string: pdfSubject2, attributes: [NSAttributedString.Key.font: fontSubject])
        let stringSizeSubject2 = attributedPDFSubject2.size()
        let stringRectSubject2 = CGRect(x: 60, y: 180, width: stringSizeSubject2.width, height: stringSizeSubject2.height)
        attributedPDFSubject2.draw(in: stringRectSubject2)
        // Drawing refer 1 :
        let attributedPDFText2 = NSAttributedString(string: pdfText2, attributes: [NSAttributedString.Key.font: fontText])
        let stringSizeText2 = attributedPDFText2.size()
        let stringRectText2 = CGRect(x: 60, y: 220, width: stringSizeText2.width, height: stringSizeText2.height)
        attributedPDFText2.draw(in: stringRectText2)
        // Drawing the "Reference 2" below
        let attributedPDFSubject3 = NSAttributedString(string: pdfSubject3, attributes: [NSAttributedString.Key.font: fontSubject])
        let stringSizeSubject3 = attributedPDFSubject3.size()
        let stringRectSubject3 = CGRect(x: 60, y: 260, width: stringSizeSubject3.width, height: stringSizeSubject3.height)
        attributedPDFSubject3.draw(in: stringRectSubject3)
        // Drawing refer 2 :
        let attributedPDFText3 = NSAttributedString(string: pdfText3, attributes: [NSAttributedString.Key.font: fontText])
        let stringSizeText3 = attributedPDFText3.size()
        let stringRectText3 = CGRect(x: 60, y: 300, width: stringSizeText3.width, height: stringSizeText3.height)
        attributedPDFText3.draw(in: stringRectText3)
        // Drawing the "postcode:" below
        let attributedPDFSubject4 = NSAttributedString(string: pdfSubject4, attributes: [NSAttributedString.Key.font: fontSubject])
        let stringSizeSubject4 = attributedPDFSubject4.size()
        let stringRectSubject4 = CGRect(x: 60, y: 340, width: stringSizeSubject4.width, height: stringSizeSubject4.height)
        attributedPDFSubject4.draw(in: stringRectSubject4)
        // Drawing postcode:
        let attributedPDFText4 = NSAttributedString(string: pdfText4, attributes: [NSAttributedString.Key.font: fontText])
        let stringSizeText4 = attributedPDFText4.size()
        let stringRectText4 = CGRect(x: 60, y: 380, width: stringSizeText4.width, height: stringSizeText4.height)
        attributedPDFText4.draw(in: stringRectText4)
        //Drawing score:
        let attributedPDFSubject5 = NSAttributedString(string: pdfSubject5, attributes: [NSAttributedString.Key.font: fontSubject])
        let stringSizeSubject5 = attributedPDFSubject5.size()
        let stringRectSubject5 = CGRect(x: 60, y: 420, width: stringSizeSubject5.width, height: stringSizeSubject5.height)
        attributedPDFSubject5.draw(in: stringRectSubject5)
        // real score
        let attributedPDFText5 = NSAttributedString(string: pdfText5, attributes: [NSAttributedString.Key.font: fontText])
        let stringSizeText5 = attributedPDFText5.size()
        let stringRectText5 = CGRect(x: 60, y: 460, width: stringSizeText5.width, height: stringSizeText5.height)
        attributedPDFText5.draw(in: stringRectText5)
        //Drawing score input by user:
        let attributedPDFSubject6 = NSAttributedString(string: pdfSubject6, attributes: [NSAttributedString.Key.font: fontSubject])
        let stringSizeSubject6 = attributedPDFSubject6.size()
        let stringRectSubject6 = CGRect(x: 60, y: 500, width: stringSizeSubject6.width, height: stringSizeSubject6.height)
        attributedPDFSubject6.draw(in: stringRectSubject6)
        // real score
        let attributedPDFText6 = NSAttributedString(string: pdfText6, attributes: [NSAttributedString.Key.font: fontText])
        let stringSizeText6 = attributedPDFText6.size()
        let stringRectText6 = CGRect(x: 60, y: 540, width: stringSizeText6.width, height: stringSizeText6.height)
        attributedPDFText6.draw(in: stringRectText6)
        // Closes the current PDF context and ends writing to the file.
        UIGraphicsEndPDFContext()
//        messageCreatedPDF()
    }
    @IBAction func viewPDF(_ sender: Any)
    {        //In case of clicking View before Create
        guard filePath != "" else {
            messageErrorPDF()
            return
        }
        let pdfView = PDFView(frame: view.bounds)
        pdfView.autoScales = true
        pdfView.tag = 100 //We assign tag 100 to control it in the removeSubView
        view.addSubview(pdfView)
        let pdfDocument = PDFDocument(url: URL(fileURLWithPath: filePath))!
        pdfView.document = pdfDocument
        
        //So that the visualization disappears when pressing on the screen
        let tapGesture = UITapGestureRecognizer(target:self, action: #selector(removeSubView))
        view.addGestureRecognizer(tapGesture)
    }
    
    @objc func removeSubView(){
        if let viewWithTag = self.view.viewWithTag(100) {
            viewWithTag.removeFromSuperview()
        }else{
            print("Don't removing anything")
        }
    }
    
    func messageCreatedPDF() {
        let alert = UIAlertController(title: "Message", message: "Do you want to create a PDF?", preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "Yes", style: UIAlertAction.Style.default){
            UIAlertAction in
            self.confirmCreation()
        })
       alert.addAction(UIAlertAction(title: "No", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    func confirmCreation(){
    let alert = UIAlertController(title: "Congratualations!", message: "You have successfully created a PDF!", preferredStyle: UIAlertController.Style.alert)
    alert.addAction(UIAlertAction(title: "Yes", style: UIAlertAction.Style.default){
    UIAlertAction in
    self.create()
        self.btCreatePDF.isEnabled = false
    })
        self.present(alert, animated: true, completion: nil)
    }
    
    func messageErrorPDF() {
        let alert = UIAlertController(title: "Error", message: "First you have to create the PDF", preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func sharePDF(_ sender: Any) {
        //In case of clicking Share before Create
        guard filePath != "" else {
            messageErrorPDF()
            return
        }
        let dc = UIDocumentInteractionController(url: URL(fileURLWithPath: filePath))
        dc.delegate = self
        dc.presentPreview(animated: true)
    }
    
    func documentInteractionControllerViewControllerForPreview(_ controller: UIDocumentInteractionController) -> UIViewController {
        return self
    }
    
    
}
