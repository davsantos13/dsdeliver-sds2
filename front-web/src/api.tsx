import axios from "axios";

const API_URL = 'https://david-dsdeliver.herokuapp.com'

export function fetchProducts() {
  return axios(`${API_URL}/products`);
}