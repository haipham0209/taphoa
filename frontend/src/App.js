import React, { useEffect, useState } from 'react';

function App() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/products")
      .then((res) => res.json())
      .then((data) => setProducts(data));
  }, []);

  return (
    <div>
      <h1>Sản phẩm</h1>
      <ul>
        {products.map((p) => (
          <li key={p.id}>
            {p.productName} - {p.price.toLocaleString()}₫
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
