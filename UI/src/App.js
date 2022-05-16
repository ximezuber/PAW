import './App.css';
import {BrowserRouter as Router, Routes, Route, Navigate, useNavigate} from "react-router-dom";
import Home from './Components/Pages/Home'
import Navbar from "./Components/NavBar";
import AdminHome from "./Components/Pages/AdminHome";
import Locations from "./Components/Pages/Locations";
import Specialties from "./Components/Pages/Specialties";
import Clinics from "./Components/Pages/Clinics";
import Prepaids from "./Components/Pages/Prepaids";
import SignUp from "./Components/Pages/SignUp";
import ClinicPrepaids from './Components/Pages/ClinicPrepaids';
import Doctors from "./Components/Pages/Doctors";
import WrappedLogin from "./Components/Pages/Login";
import "../src/i18n/i18n"
import DoctorHome from "./Components/Pages/DoctorHome";
import DoctorClinics from "./Components/Pages/DoctorClinics";
import DoctorClinicSchedule from "./Components/Pages/DoctorClinicSchedule";
import UserDoctorProfile from "./Components/Pages/UserDoctorProfile";
import Favorites from "./Components/Pages/Favorites";
import Profile from "./Components/Pages/Profile";
import Appointments from "./Components/Pages/Appointments";
import {useState} from "react";
import apiCalls from "./api/apiCalls";

function App() {
    const [role, setRole] = useState(localStorage.getItem('role'));

    const isAuth = () => role !== null;

    const isAdmin = () => isAuth(role)? role === 'ROLE_ADMIN' : false;
    const isDoc = () => isAuth(role)? role === 'ROLE_DOCTOR' : false;
    const isUser = () => isAuth(role)? role === 'ROLE_USER' : false;

    function AdminRoute({ children }) {
        const auth = isAdmin();
        return auth ? children : <Navigate to="/paw-2019b-4/login" />;
    }

    function DoctorRoute ({children}) {
        const auth = isDoc();
        return auth ? children : <Navigate to="/paw-2019b-4/login" />;
    }

    function UserRoute ({children}) {
        const auth = isUser();
        return auth ? children : <Navigate to="/paw-2019b-4/login" />;
    }

    function handleLogout ()  {
        apiCalls.logout();
        setRole(null);
    }

    function handleRoleLogin (role) {
        setRole(role)
        localStorage.setItem('role', role)
    }

  return (
    <div className="App">
        <div className="App-header">
            <Router>
                <Navbar isAuth={isAuth} role={role} setRole={setRole}/>
                <Routes>
                    <Route exact path='/paw-2019b-4' element={<Home logout={handleLogout} />}/>
                    <Route exact path='/paw-2019b-4/appointments' element={<UserRoute><Appointments logout={handleLogout} user="patient" /></UserRoute>}/>
                    <Route exact path='/paw-2019b-4/:license/profile' element={<UserDoctorProfile logout={handleLogout} isUser={isUser}/>}/>
                    <Route exact path="/paw-2019b-4/doctor" element={<DoctorRoute><DoctorHome logout={handleLogout} /></DoctorRoute>} />
                    <Route exact path="/paw-2019b-4/doctor/clinics" element={<DoctorRoute><DoctorClinics logout={handleLogout} /></DoctorRoute>} />
                    <Route exact path="/paw-2019b-4/doctor/appointments" element={<DoctorRoute><Appointments logout={handleLogout} user="doctor" /></DoctorRoute>} />
                    <Route exact path="/paw-2019b-4/doctor/:license/clinics/:id/schedule"
                           element={<DoctorRoute><DoctorClinicSchedule logout={handleLogout} /></DoctorRoute>} />
                    <Route exact path='/paw-2019b-4/admin/' exact element={<AdminRoute><AdminHome logout={handleLogout} /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/locations' element={<AdminRoute><Locations logout={handleLogout} /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/specialties' element={<AdminRoute><Specialties logout={handleLogout} /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/clinics' element={<AdminRoute><Clinics logout={handleLogout} /></AdminRoute>} />
                    <Route exact path='/paw-2019b-4/admin/prepaids' element={<AdminRoute><Prepaids logout={handleLogout} /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/clinics/:id/prepaids' element={<AdminRoute><ClinicPrepaids logout={handleLogout} /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/doctors' element={<AdminRoute><Doctors logout={handleLogout} /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/login' element={<WrappedLogin setRole={handleRoleLogin} logout={handleLogout} />}/>
                    <Route exact path='/paw-2019b-4/signUp' element={<SignUp logout={handleLogout} setRole={handleRoleLogin} />}/>
                    <Route exact path='/paw-2019b-4/favorites' element={<UserRoute><Favorites logout={handleLogout} /></UserRoute>}/>
                    <Route exact path='/paw-2019b-4/profile' element={<UserRoute><Profile logout={handleLogout} /></UserRoute>}/>
                </Routes>
            </Router>
        </div>
    </div>
  );
}

export default App;
