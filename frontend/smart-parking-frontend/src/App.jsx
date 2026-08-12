import AppRoutes from "./routes/AppRoutes";
import { ToastContainer, Slide } from "react-toastify";

function App() {
  return (
    <>
      <AppRoutes />

      <ToastContainer
        position="top-right"
        autoClose={3000}
        limit={3}
        newestOnTop
        hideProgressBar
        closeOnClick
        pauseOnHover
        draggable
        theme="colored"
        transition={Slide}
      />
    </>
  );
}

export default App;
