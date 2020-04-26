export default class SubURL {
  constructor(subURL, subPageName) {
    this.subURL = subURL;
    this.subPageName = subPageName;
  }

  toString() {
    return { subURL: this.subURL, subPageName: this.subPageName };
  }
}
