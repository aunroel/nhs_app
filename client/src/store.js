import { action } from "easy-peasy";

const storeModel = {
  todo: {
    todoList: [],
    addItem: action((todo, item) => {
      todo.todoList.push(item);
    }),
    overwriteList: action((todo, list) => {
      todo.todoList = list;
    })
  }
};

export default storeModel;
