//
//  Model.swift
//  GapdarMyPages
//
//  Created by Davinder Bassan on 09/03/2020.
//  Copyright © 2020 localadmin. All rights reserved.
//

import Foundation
import Firebase
import FirebaseMLCommon

var model: LocalModel!
var interpreter: ModelInterpreter!
var ioOptions: ModelInputOutputOptions!

private func loadModel() -> Bool {
  guard let modelPath = Bundle.main.path(forResource: "model", ofType: "tflite")
  else {
    // Invalid model path
    print("invalid model path")
    return false
  }
  model = LocalModel(
    name: "model",
    path: modelPath
  )
  let registrationSuccessful = ModelManager.modelManager().register(model)
  if (!registrationSuccessful) {
    print("registration failed")
    return false
  }
  return true
}


private func buildInterpreter() {
  let options = ModelOptions(remoteModelName: nil, localModelName: "model")
  let _interpreter = ModelInterpreter.modelInterpreter(options: options)
  let _ioOptions = ModelInputOutputOptions()
  do {
    try _ioOptions.setInputFormat(index: 0, type: .float32, dimensions: [1, 28, 28, 1])
    try _ioOptions.setOutputFormat(index: 0, type: .float32, dimensions: [1, 1000])
  } catch let error as NSError {
    print("error setting up model io: \(error)")
  }
  // initialize members
  interpreter = _interpreter
  ioOptions = _ioOptions
}


private func prepInputData(){
    let inputs = ModelInputs()
    var inputData = Data()
    for pixel in inputArray {
      var f = Float32(pixel)
      let elementSize = MemoryLayout.size(ofValue: f)
      var bytes = [UInt8](repeating: 0, count: elementSize)
      memcpy(&bytes, &f, elementSize)
      inputData.append(&bytes, count: elementSize)
    }
    do {
      try inputs.addInput(inputData)
    } catch let error {
      print("add input failure: \(error)")
    }
}

private func runModel(){
    interpreter.run(inputs: inputs, options: ioOptions) {
     outputs, error in
       guard error == nil, let outputs = outputs else {
         print("interpreter error")
         if (error != nil) {
           print(error!)
         }
         return
       }
       do {
         let result = try outputs.output(index: 0) as! [[NSNumber]]
         let floatArray = result[0].map {
           a in
           a.floatValue // get out the float value
         }
         let top = intArray.max()
         let label = intArray.firstIndex(of: top!)! + 1
       } catch { // error }
    }
}
