import React, { useState } from "react";
import { useStoreState, useStoreActions } from "easy-peasy";
import storeModel from "../../../store/store";

const TodoList = () => {
  const [savedList, saveList] = [
    useStoreState(state => state.todoList.todoList),
    useStoreActions(state => state.todoList.overwriteList)
  ];

  const [list, setList] = useState(savedList);
  const [listModified, setListModified] = useState(false);

  const handleSubmit = event => {
    event.preventDefault();
    const inputBox = document.getElementById("new-todo");
    const inputText = inputBox.value;

    setList([...list, inputText]);
    setListModified(true);
    inputBox.value = "";
  };

  const onSaveClick = () => {
    saveList(list);
    setListModified(false);
  };

  return (
    <div>
      <h3>TO DO list</h3>

      <ul>
        {list.map(item => (
          <li key={item.id}>{item}</li>
        ))}
      </ul>

      <form onSubmit={handleSubmit}>
        <label htmlFor="new-todo">What needs to be done?</label>
        <input id="new-todo" type="text" autoFocus />
        <button>Add #{list.length + 1}</button>
      </form>
      <br />
      <button onClick={onSaveClick}>Save list</button>
      {listModified ? <div>Click save to save your updates!</div> : null}
    </div>
  );
};

export default InputField;
