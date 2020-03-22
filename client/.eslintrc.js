module.exports = {
  env: {
    browser: true,
    es6: true
  },
  extends: [
    "plugin:react/recommended",
    // "client\\node_modules\\eslint-plugin-react",
    "airbnb",
    "prettier"
  ],
  globals: {
    Atomics: "readonly",
    SharedArrayBuffer: "readonly"
  },
  parser: "@typescript-eslint/parser",
  parserOptions: {
    ecmaFeatures: {
      jsx: true
    },
    ecmaVersion: 2018,
    sourceType: "module"
  },
  plugins: ["react", "@typescript-eslint"],
  rules: {
    "no-nested-ternary": "warn",
    "prefer-template": "off",

    "react/jsx-filename-extension": [
      1,
      { extensions: [".js", ".jsx", ".tsx"] }
    ],
    "react/prop-types": "warn",
    "react/jsx-props-no-spreading": "off",
    "import/prefer-default-export": "off"
  }
};
