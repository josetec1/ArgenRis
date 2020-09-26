import React, { useState } from 'react';
import clsx from 'clsx';
import PropTypes from 'prop-types';
// import moment from 'moment';
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
  makeStyles
} from '@material-ui/core';
// import getInitials from 'src/utils/getInitials';

const useStyles = makeStyles(theme => ({
  root: {},
  avatar: {
    marginRight: theme.spacing(2)
  }
}));

const Results = ({
  className, isLoaded, pacientes, ...rest
}) => {
  const classes = useStyles();
  const [selectedPacienteId, setSelectedPacienteId] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);

  const handleSelectAll = event => {
    let newSelectedPacienteId;

    if (event.target.checked) {
      newSelectedPacienteId = pacientes.map(paciente => paciente.id);
    } else {
      newSelectedPacienteId = [];
    }

    setSelectedPacienteId(newSelectedPacienteId);
  };

  const handleSelectOne = (event, id) => {
    const selectedIndex = selectedPacienteId.indexOf(id);
    let newSelectedPacienteId = [];

    if (selectedIndex === -1) {
      newSelectedPacienteId = newSelectedPacienteId.concat(selectedPacienteId, id);
    } else if (selectedIndex === 0) {
      newSelectedPacienteId = newSelectedPacienteId.concat(selectedPacienteId.slice(1));
    } else if (selectedIndex === selectedPacienteId.length - 1) {
      newSelectedPacienteId = newSelectedPacienteId.concat(selectedPacienteId.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelectedPacienteId = newSelectedPacienteId.concat(
        selectedPacienteId.slice(0, selectedIndex),
        selectedPacienteId.slice(selectedIndex + 1)
      );
    }

    setSelectedPacienteId(newSelectedPacienteId);
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
                    checked={selectedPacienteId.length === pacientes.length}
                    color='primary'
                    indeterminate={
                      selectedPacienteId.length > 0
                      && selectedPacienteId.length < pacientes.length
                    }
                    onChange={handleSelectAll}
                  />
                </TableCell>
                <TableCell>
                  Nombre y Apellido
                </TableCell>
                <TableCell>
                  DNI
                </TableCell>
                <TableCell>
                  Email
                </TableCell>
                <TableCell>
                  Direccion
                </TableCell>
                <TableCell>
                  Telefono
                </TableCell>
                <TableCell>
                  Fecha de Nacimiento
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {pacientes.slice(0, limit).map(paciente => (
                <TableRow
                  hover
                  key={paciente.id}
                  selected={selectedPacienteId.indexOf(paciente.id) !== -1}
                >
                  <TableCell padding='checkbox'>
                    <Checkbox
                      checked={selectedPacienteId.indexOf(paciente.id) !== -1}
                      onChange={event => handleSelectOne(event, paciente.id)}
                      value='true'
                    />
                  </TableCell>
                  <TableCell>
                    <Box
                      alignItems='center'
                      display='flex'
                    >
                      {/* <Avatar
                        className={classes.avatar}
                        src={paciente.avatarUrl}
                      >
                        {getInitials(paciente.name)}
                      </Avatar> */}
                      <Typography
                        color='textPrimary'
                        variant='body1'
                      >
                        {`${paciente.nombre} ${paciente.apellido}`}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    {paciente.dni}
                  </TableCell>
                  <TableCell>
                    {paciente.email}
                  </TableCell>
                  <TableCell>
                    {paciente.direccion}
                  </TableCell>
                  <TableCell>
                    {paciente.telefono}
                  </TableCell>
                  <TableCell>
                    {paciente.fechaDeNacimiento}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </PerfectScrollbar>
      <TablePagination
        component='div'
        count={pacientes.length}
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
  pacientes: PropTypes.array.isRequired,
  isLoaded: PropTypes.bool.isRequired
};

export default Results;
