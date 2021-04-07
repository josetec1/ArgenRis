import React, { useState } from 'react';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import moment from 'moment';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {
  // Avatar,
  CircularProgress,
  Box,
  Card,
  Checkbox,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  Typography,
  makeStyles,
} from '@material-ui/core';
// import getInitials from 'src/utils/getInitials';

const useStyles = makeStyles(theme => ({
  root: {},
  avatar: {
    marginRight: theme.spacing(2)
  }
}));

const Results = ({
  className, citas, ...rest
}) => {
  const classes = useStyles();
  const [selectedCitaId, setSelectedCitaId] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);

  const handleSelectAll = event => {
    let newSelectedOrdenId;

    if (event.target.checked) {
      newSelectedOrdenId = citas.map(cita => cita.id);
    } else {
      newSelectedOrdenId = [];
    }

    setSelectedCitaId(newSelectedOrdenId);
  };

  const handleSelectOne = (event, id) => {
    const selectedIndex = selectedCitaId.indexOf(id);
    let newSelectedOrdenId = [];

    if (selectedIndex === -1) {
      newSelectedOrdenId = newSelectedOrdenId.concat(selectedCitaId, id);
    } else if (selectedIndex === 0) {
      newSelectedOrdenId = newSelectedOrdenId.concat(selectedCitaId.slice(1));
    } else if (selectedIndex === selectedCitaId.length - 1) {
      newSelectedOrdenId = newSelectedOrdenId.concat(selectedCitaId.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelectedOrdenId = newSelectedOrdenId.concat(
        selectedCitaId.slice(0, selectedIndex),
        selectedCitaId.slice(selectedIndex + 1)
      );
    }

    setSelectedCitaId(newSelectedOrdenId);
  };

  const handleLimitChange = event => {
    setLimit(event.target.value);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return citas ? (
    <Card
      className={clsx(classes.root, className)}
      {...rest}
    >
      <PerfectScrollbar>
        <Box minWidth={1050}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell padding='checkbox'>
                  <Checkbox
                    checked={selectedCitaId.length === citas.length}
                    color='primary'
                    indeterminate={
                      selectedCitaId.length > 0
                      && selectedCitaId.length < citas.length
                    }
                    onChange={handleSelectAll}
                  />
                </TableCell>
                <TableCell>
                  Cita ID
                </TableCell>
                <TableCell>
                  Prioridad
                </TableCell>
                <TableCell>
                  Fecha de cita
                </TableCell>
                <TableCell>
                  Horario de cita
                </TableCell>
                <TableCell>
                  Estado de Cita
                </TableCell>
                <TableCell>
                  
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {citas.sort((a, b) => a.pacienteID - b.pacienteID).slice(0, limit).map(cita => (
                <TableRow
                  hover
                  key={cita.id}
                  selected={selectedCitaId.indexOf(cita.id) !== -1}
                >
                  <TableCell padding='checkbox'>
                    <Checkbox
                      checked={selectedCitaId.indexOf(cita.id) !== -1}
                      onChange={event => handleSelectOne(event, cita.id)}
                      value='true'
                    />
                  </TableCell>
                  <TableCell>
                    <Box
                      alignItems='center'
                      display='flex'
                    >
                      <Typography
                        color='textPrimary'
                        variant='body1'
                      >
                        {cita.pacienteID}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    {cita.id}
                  </TableCell>
                  <TableCell>
                    {cita.prioridad}
                  </TableCell>
                  <TableCell>
                    {`${moment(cita.fechaYHoraDeCita).format('DD-MM-YYYY')}`}
                  </TableCell>
                  <TableCell>
                    {`${moment(cita.fechaYHoraDeCita).format('hh:mm')}`}
                  </TableCell>
                  <TableCell>
                    {cita.estadoDeCitaID}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </PerfectScrollbar>
      <TablePagination
        component='div'
        count={citas.length}
        onChangePage={handlePageChange}
        onChangeRowsPerPage={handleLimitChange}
        page={page}
        rowsPerPage={limit}
        rowsPerPageOptions={[5, 10, 25]}
      />
    </Card>
  ) : <CircularProgress variant='indeterminate' color='secondary' />;
};

Results.propTypes = {
  className: PropTypes.string,
  citas: PropTypes.array.isRequired,
  isLoaded: PropTypes.bool.isRequired
};

export default Results;
