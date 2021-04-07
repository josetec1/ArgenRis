import React from 'react';
import {
  Box,
  Container,
  makeStyles
} from '@material-ui/core';
import Page from 'src/components/Page';
import Results from './OrdenesListView/Results';

import useOrdenesSearch from './useOrdenesSearch';

const useStyles = makeStyles(theme => ({
  root: {
    backgroundColor: theme.palette.background.dark,
    minHeight: '100%',
    paddingBottom: theme.spacing(3),
    paddingTop: theme.spacing(3)
  }
}));

const MedicoOrdenesView = () => {
  const classes = useStyles();
  const {
    ordenes,
    isLoaded
  } = useOrdenesSearch();

  return (
    <Page
      className={classes.root}
      title='Ordenes'
    >
      <Container maxWidth={false}>
        <Box mt={3}>
          <Results ordenes={ordenes} isLoaded={isLoaded} />
        </Box>
      </Container>
    </Page>
  );
};

export default MedicoOrdenesView;
