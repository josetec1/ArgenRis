class AuthenticationService {
  constructor() {
    this.authenticated = false;
    this.rol = 'paciente';
    this.authUserId = 1;
  }

  login(cb) {
    this.authenticated = true;
    cb(this.rol);
  }

  logout(cb) {
    this.authenticated = false;
    cb();
  }

  isAuthenticated() {
    return this.authenticated;
  }

  isRol(rol) {
    return rol ? this.rol === rol : true;
  }

  getUserId() {
    return this.authUserId;
  }
}

export default new AuthenticationService();
