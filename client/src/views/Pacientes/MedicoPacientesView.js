import React from 'react';
import {
  Box,
  Container,
  makeStyles
} from '@material-ui/core';
import Page from 'src/components/Page';
import Results from './PacientesListView/Results';
import Toolbar from './PacientesListView/Toolbar';
// import data from './data';

import usePacientesSearch from './usePacientesSearch';

const useStyles = makeStyles(theme => ({
  root: {
    backgroundColor: theme.palette.background.dark,
    minHeight: '100%',
    paddingBottom: theme.spacing(3),
    paddingTop: theme.spacing(3)
  }
}));

const CustomerListView = () => {
  const classes = useStyles();
  const {
    pacientes,
    isLoaded,
    searchPacientesByName
  } = usePacientesSearch();

  return (
    <Page
      className={classes.root}
      title='Pacientes'
    >
      <Container maxWidth={false}>
        <Toolbar onSearchChange={searchPacientesByName} />
        <Box mt={3}>
          <Results pacientes={pacientes} isLoaded={isLoaded} />
        </Box>
      </Container>
    </Page>
  );
};

export default CustomerListView;
