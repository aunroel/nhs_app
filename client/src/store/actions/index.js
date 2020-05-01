/* eslint-disable no-restricted-syntax */
export const REGISTER_SUCCESS = "REGISTER_SUCCESS";
// export const REGISTER_FAIL = "REGISTER_FAIL";

export function validateType(obj, types) {
  if (Object.keys(obj).length !== Object.keys(types).length) {
    throw new TypeError("wrong type");
  }

  for (const [key, value] of Object.entries(obj)) {
    if (!types[key] || (value != null && types[key] !== typeof value)) {
      throw new TypeError(`key ${key} value ${value}`);
    }
  }
}
