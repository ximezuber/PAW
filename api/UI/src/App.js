import './App.css';
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
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

function App() {

    const isAuth = () => localStorage.getItem('role') !== null;

    const isAdmin = () => isAuth()? localStorage.getItem('role') === 'ROLE_ADMIN' : false;
    const isDoc = () => isAuth()? localStorage.getItem('role') === 'ROLE_DOCTOR' : false;
    const isUser = () => isAuth()? localStorage.getItem('role') === 'ROLE_USER' : false;

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

  return (
    <div className="App">
        <div className="App-header">
            <Router>
                <Navbar isAuth={isAuth}/>
                <Routes>
                    <Route exact path='/paw-2019b-4' element={<Home/>}/>
                    <Route exact path='/paw-2019b-4/appointments' element={<UserRoute><Appointments user="patient" /></UserRoute>}/>
                    <Route exact path='/paw-2019b-4/:license/profile' element={<UserDoctorProfile />}/>
                    <Route exact path="/paw-2019b-4/doctor" element={<DoctorRoute><DoctorHome /></DoctorRoute>} />
                    <Route exact path="/paw-2019b-4/doctor/clinics" element={<DoctorRoute><DoctorClinics/></DoctorRoute>} />
                    <Route exact path="/paw-2019b-4/doctor/appointments" element={<DoctorRoute><Appointments user="doctor" /></DoctorRoute>} />
                    <Route exact path="/paw-2019b-4/doctor/:license/clinics/:id/schedule"
                           element={<DoctorRoute><DoctorClinicSchedule /></DoctorRoute>} />
                    <Route exact path='/paw-2019b-4/admin/' exact element={<AdminRoute><AdminHome /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/locations' element={<AdminRoute><Locations /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/specialties' element={<AdminRoute><Specialties /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/clinics' element={<AdminRoute><Clinics /></AdminRoute>} />
                    <Route exact path='/paw-2019b-4/admin/prepaids' element={<AdminRoute><Prepaids /></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/clinics/:id/prepaids' element={<AdminRoute><ClinicPrepaids/></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/admin/doctors' element={<AdminRoute><Doctors/></AdminRoute>}/>
                    <Route exact path='/paw-2019b-4/login' element={<WrappedLogin />}/>
                    <Route exact path='/paw-2019b-4/signUp' element={<SignUp />}/>
                    <Route exact path='/paw-2019b-4/favorites' element={<UserRoute><Favorites /></UserRoute>}/>
                    <Route exact path='/paw-2019b-4/profile' element={<UserRoute><Profile/></UserRoute>}/>
                </Routes>
            </Router>
        </div>
    </div>
  );
}

export default App;
