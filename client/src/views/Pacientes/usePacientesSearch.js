import { useState, useEffect, useCallback } from 'react';

const usePacientesSearch = () => {
  const [isLoaded, setIsLoaded] = useState(false);
  const [pacientes, setPacientes] = useState([]);

  // Note: the empty deps array [] means
  // this useEffect will run once
  // similar to componentDidMount()

  const searchPacientes = useCallback(async () => {
    try {
      await fetch('http://localhost:8080/pacientes')
        .then(res => res.json())
        .then(
          result => {
            setIsLoaded(true);
            setPacientes(result);
          }
        );
    } catch (e) {
      console.log(e);
    }
  }, []);

  const searchPacientesByName = useCallback(async name => {
    try {
      await fetch(`http://localhost:8080/pacientes/buscarpornombre/?nombre=${name.toString()}`)
        .then(res => res.json())
        .then(
          result => {
            setIsLoaded(true);
            setPacientes(result);
          }
        );
    } catch (e) {
      console.log(e);
    }
  }, []);

  useEffect(() => {
    searchPacientes();
  }, [searchPacientes]);

  return {
    pacientes,
    isLoaded,
    searchPacientes,
    searchPacientesByName
  };
};

export default usePacientesSearch;
