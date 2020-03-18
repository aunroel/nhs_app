import React from "react";
import { Switch, Route, useRouteMatch, useParams } from "react-router-dom";
import LinksList from "../Components/LinksList";
import SubURL from "../Classes/SubURL";

const subsubUrls = [
  new SubURL("subsub1", "Sub sub 1"),
  new SubURL("subsub2", "Sub sub 2"),
  new SubURL("subsub3", "Sub sub 3")
];

console.table(subsubUrls);

const SubPageBox = () => {
  const { url, path } = useRouteMatch();
  const { subroute } = useParams();

  console.table({ url, path, subroute });

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

const SubSubPageBox = () => {
  const { subsub } = useParams();
  return <h4>{subsub}</h4>;
};

export default SubPageBox;
