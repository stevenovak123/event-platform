
# Fixes

## Part 6: Creating a new event:
Url is `/dashboard/create-event` shown in the video

Actual: `/dashboard/events/create` - Update the URL on the button in the `organizers-landing-page.tsx`

In `dashboard-manage-event-page.tsx` - commented out `navigate("/dashboard/events");` to avoid reloading of page on clicking submit


## Part 7: List all Events
URL is `/dashboard/list-events` shown in the video.

Actual: `/dashboard/events` - Add `className="cursor-pointer"
                onClick={() => navigate("/dashboard/events")}` to the browse events button in the `organizers-landing-page.tsx`




## UI updates: Date Time Picker width increase
Present in the file `dashboard-manage-event-page.tsx` increased width to be able to display the time button.
## Redirect issues. 
Removed from `main.tsx` the `/login` route completely
update the code in `callback-page.tsx` 
```javascript
 useEffect(() => {
    if (isLoading) {
      return;
    }
    console.log("Auth is done loading. Authenticated:", isAuthenticated);
    if (isAuthenticated) {
      const redirectPath = localStorage.getItem("redirectPath");
      if (redirectPath) {
        localStorage.removeItem("redirectPath");
        navigate(redirectPath);
      } else {
        console.log("No redirectPath found, navigating to home.");
        navigate("/"); // fallback
      }
    }
  }, [isLoading, isAuthenticated, navigate]);
  ```
update the code in `protected-route.tsx`
```js
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
  
```



## Added a button before logging to show basic credentials in `attendee-landing-page.tsx`

```js
 const showCreds = () => {
    alert("This a test website. There are 3 users. Attendee, Organiser, Staff");
    alert(
      "Usernames are attendee, staff, organiser. Password for all is password123",
    );
  };
```
updated the login part of the ternary operator to 
```jsx
 <>
            <div className="flex gap-4 pr-1">
              <Button className="cursor-pointer" onClick={() => showCreds()}>
                Show Credentials
              </Button>
            </div>
            <div className="flex gap-4">
              <Button
                className="cursor-pointer"
                onClick={() => signinRedirect()}
              >
                Log in
              </Button>
            </div>
          </>
```

## Added a `<Link>` to home in the `nav-bar.tsx`

## Updated the code in `dashboard-list-tickets.tsx` to below.
change pageable to pagination.
```jsx
 {tickets && (
          <SimplePagination pagination={tickets} onPageChange={setPage} />
        )}
```






























# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type-aware lint rules:

```js
export default tseslint.config({
  extends: [
    // Remove ...tseslint.configs.recommended and replace with this
    ...tseslint.configs.recommendedTypeChecked,
    // Alternatively, use this for stricter rules
    ...tseslint.configs.strictTypeChecked,
    // Optionally, add this for stylistic rules
    ...tseslint.configs.stylisticTypeChecked,
  ],
  languageOptions: {
    // other options...
    parserOptions: {
      project: ["./tsconfig.node.json", "./tsconfig.app.json"],
      tsconfigRootDir: import.meta.dirname,
    },
  },
});
```

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from "eslint-plugin-react-x";
import reactDom from "eslint-plugin-react-dom";

export default tseslint.config({
  plugins: {
    // Add the react-x and react-dom plugins
    "react-x": reactX,
    "react-dom": reactDom,
  },
  rules: {
    // other rules...
    // Enable its recommended typescript rules
    ...reactX.configs["recommended-typescript"].rules,
    ...reactDom.configs.recommended.rules,
  },
});
```


