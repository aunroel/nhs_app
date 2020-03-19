import { action } from "easy-peasy";

export const debugStoreModel = {
  mlModels: [],
  addMLModel: action((debugStore, newMLModel) => {
    debugStore.mlModels.push(newMLModel);
  })
};

export const todoList = {
  todoList: [],
  addItem: action((todo, item) => {
    todo.todoList.push(item);
  }),
  overwriteList: action((todo, list) => {
    todo.todoList = list;
  })
};
