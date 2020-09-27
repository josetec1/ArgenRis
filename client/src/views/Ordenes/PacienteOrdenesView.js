import React from 'react';
import {
  Box,
  Container,
  makeStyles
} from '@material-ui/core';
import Page from 'src/components/Page';
import Results from './OrdenesListView/Results';
import AuthenticationService from '../../components/Authentication/AuthenticationService';
import useOrdenesSearch from './useOrdenesSearch';

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
    ordenes,
    isLoaded,
  } = useOrdenesSearch();

  const pacienteOrdenes = ordenes.filter(orden => orden.pacienteID === AuthenticationService.getUserId());
  return (
    <Page
      className={classes.root}
      title='Ordenes'
    >
      <Container maxWidth={false}>
        <Box mt={3}>
          <Results ordenes={pacienteOrdenes} isLoaded={isLoaded} />
        </Box>
      </Container>
    </Page>
  );
};

export default CustomerListView;
