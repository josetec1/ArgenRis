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
  className, isLoaded, ordenes, ...rest
}) => {
  const classes = useStyles();
  const [selectedOrdenId, setSelectedOrdenId] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);

  const handleSelectAll = event => {
    let newSelectedOrdenId;

    if (event.target.checked) {
      newSelectedOrdenId = ordenes.map(orden => orden.id);
    } else {
      newSelectedOrdenId = [];
    }

    setSelectedOrdenId(newSelectedOrdenId);
  };

  const handleSelectOne = (event, id) => {
    const selectedIndex = selectedOrdenId.indexOf(id);
    let newSelectedOrdenId = [];

    if (selectedIndex === -1) {
      newSelectedOrdenId = newSelectedOrdenId.concat(selectedOrdenId, id);
    } else if (selectedIndex === 0) {
      newSelectedOrdenId = newSelectedOrdenId.concat(selectedOrdenId.slice(1));
    } else if (selectedIndex === selectedOrdenId.length - 1) {
      newSelectedOrdenId = newSelectedOrdenId.concat(selectedOrdenId.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelectedOrdenId = newSelectedOrdenId.concat(
        selectedOrdenId.slice(0, selectedIndex),
        selectedOrdenId.slice(selectedIndex + 1)
      );
    }

    setSelectedOrdenId(newSelectedOrdenId);
  };

  const handleLimitChange = event => {
    setLimit(event.target.value);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return isLoaded ? (
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
                    checked={selectedOrdenId.length === ordenes.length}
                    color='primary'
                    indeterminate={
                      selectedOrdenId.length > 0
                      && selectedOrdenId.length < ordenes.length
                    }
                    onChange={handleSelectAll}
                  />
                </TableCell>
                <TableCell>
                  Paciente ID
                </TableCell>
                <TableCell>
                  Medico ID
                </TableCell>
                <TableCell>
                  Prioridad
                </TableCell>
                <TableCell>
                  Estado de la Orden
                </TableCell>
                <TableCell>
                  Nota Adicional
                </TableCell>
                <TableCell>
                  Fecha de Creacion
                </TableCell>
                <TableCell>
                  Hora de Creacion
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {ordenes.sort((a, b) => a.pacienteID - b.pacienteID).slice(0, limit).map(orden => (
                <TableRow
                  hover
                  key={orden.id}
                  selected={selectedOrdenId.indexOf(orden.id) !== -1}
                >
                  <TableCell padding='checkbox'>
                    <Checkbox
                      checked={selectedOrdenId.indexOf(orden.id) !== -1}
                      onChange={event => handleSelectOne(event, orden.id)}
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
                        {orden.pacienteID}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    {orden.medicoID}
                  </TableCell>
                  <TableCell>
                    {orden.prioridad}
                  </TableCell>
                  <TableCell>
                    {orden.estadoDeLaOrdenID}
                  </TableCell>
                  <TableCell>
                    {orden.notaAdicional}
                  </TableCell>
                  <TableCell>
                    {`${moment.utc(orden.fechaCreacionDeOrden).format('DD-MM-YYYY')}`}
                  </TableCell>
                  <TableCell>
                    {`${moment.utc(orden.fechaCreacionDeOrden).format('hh:mm')}`}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </PerfectScrollbar>
      <TablePagination
        component='div'
        count={ordenes.length}
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
  ordenes: PropTypes.array.isRequired,
  isLoaded: PropTypes.bool.isRequired
};

export default Results;
