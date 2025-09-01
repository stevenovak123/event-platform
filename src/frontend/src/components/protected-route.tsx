import { ReactNode, useEffect } from "react";
import { useAuth } from "react-oidc-context";
import { Navigate, useLocation } from "react-router";

interface ProtectedRouteProperties {
  children: ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProperties> = ({ children }) => {
  const { isLoading, isAuthenticated, signinRedirect } = useAuth();
  const location = useLocation();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      // Store attempted URL before redirecting to Keycloak
      localStorage.setItem("redirectPath", location.pathname + location.search);
      signinRedirect(); // Redirects directly to Keycloak login
    }
  }, [isLoading, isAuthenticated, location, signinRedirect]);

  if (isLoading || (!isAuthenticated && typeof window !== "undefined")) {
    return <p>Loading...</p>;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
