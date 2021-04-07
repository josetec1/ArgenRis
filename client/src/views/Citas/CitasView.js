import React from 'react';
import {
  Box,
  Container,
  makeStyles
} from '@material-ui/core';
import {
  useParams
} from 'react-router-dom';
import Page from 'src/components/Page';
import Results from './CitasListViewer/Results';

const useStyles = makeStyles(theme => ({
  root: {
    backgroundColor: theme.palette.background.dark,
    minHeight: '100%',
    paddingBottom: theme.spacing(3),
    paddingTop: theme.spacing(3)
  }
}));

const CitasView = () => {
  const classes = useStyles();
  const { citas } = useParams();

  return (
    <Page
      className={classes.root}
      title='Citas'
    >
      <Container maxWidth={false}>
        <Box mt={3}>
          <Results ordenes={citas} />
        </Box>
      </Container>
    </Page>
  );
};

export default CitasView;
