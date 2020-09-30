import { useState, useEffect, useCallback } from 'react';

const useOrdenesSearch = () => {
  const [isLoaded, setIsLoaded] = useState(false);
  const [ordenes, setOrdenes] = useState([]);

  // Note: the empty deps array [] means
  // this useEffect will run once
  // similar to componentDidMount()

  const searchOrdenes = useCallback(async () => {
    try {
      await fetch('http://localhost:8080/ordenes')
        .then(res => res.json())
        .then(
          result => {
            setIsLoaded(true);
            setOrdenes(result);
          }
        );
    } catch (e) {
      console.log(e);
    }
  }, []);

  const searchOrdenesByPacienteId = useCallback(async id => {
    try {
      await fetch('http://localhost:8080/ordenes')
        .then(res => res.json())
        .then(
          result => {
            const pacienteOrdenes = result.filter(orden => orden.pacienteID === id);
            setIsLoaded(true);
            setOrdenes(pacienteOrdenes);
          }
        );
    } catch (e) {
      console.log(e);
    }
  }, []);


  useEffect(() => {
    searchOrdenes();
  }, [searchOrdenes]);

  return {
    ordenes,
    isLoaded,
    searchOrdenes,
    searchOrdenesByPacienteId
  };
};

export default useOrdenesSearch;
