import React from 'react';
import {
  Box,
  Container,
  makeStyles
} from '@material-ui/core';
import Page from 'src/components/Page';
import Results from './Results';
import Toolbar from './Toolbar';
// import data from './data';

import useOrdenesSearch from '../useOrdenesSearch';

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
    searchOrdenesByPacienteName
  } = useOrdenesSearch();

  return (
    <Page
      className={classes.root}
      title='Ordenes'
    >
      <Container maxWidth={false}>
        <Toolbar onSearchChange={searchOrdenesByPacienteName} />
        <Box mt={3}>
          <Results ordenes={ordenes} isLoaded={isLoaded} />
        </Box>
      </Container>
    </Page>
  );
};

export default CustomerListView;
