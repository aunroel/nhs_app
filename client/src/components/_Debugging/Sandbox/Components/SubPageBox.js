import React from "react";
import { Switch, Route, useRouteMatch, useParams } from "react-router-dom";
import LinksList from "./LinksList";
import SubURL from "../Classes/SubURL";

const subsubUrls = [
  new SubURL("subsub1", "Sub sub 1"),
  new SubURL("subsub2", "Sub sub 2"),
  new SubURL("subsub3", "Sub sub 3")
];

const SubSubPageBox = () => {
  const { url } = useRouteMatch();
  const { subsub } = useParams();

  console.table({ url, subsub });

  return <h4>{subsub}</h4>;
};

const SubPageBox = () => {
  const { url } = useRouteMatch();
  const { subroute } = useParams();

  return (
    <>
      <h3>{subroute}</h3>
      <LinksList url={url} subUrlPaths={subsubUrls} />
      <Switch>
        <Route path={`${url}/:subsub`}>
          <SubSubPageBox />
        </Route>
      </Switch>
    </>
  );
};

export default SubPageBox;
