import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from "react";

interface State {
    username: string,
    email: string,
    password: string,
    repeatedPassword: string
}

export class Registration extends Component<{}, State> {

   constructor(props: any) {
       super(props);
       this.state = {username: "", email: "", password: "", repeatedPassword: ""};

       this.handleUsernameChange = this.handleUsernameChange.bind(this);
       this.handleEmailChange = this.handleEmailChange.bind(this);
       this.handlePasswordChange = this.handlePasswordChange.bind(this);
       this.handlePasswordRepeatChange = this.handlePasswordRepeatChange.bind(this);
   }

   private handleUsernameChange(event: any) {
        this.setState({username: event.target.value});
   }

   private handleEmailChange(event: any) {
        this.setState({email: event.target.value});
   }

   private handlePasswordChange(event: any) {
        this.setState({password: event.target.value});
   }

   private handlePasswordRepeatChange(event: any) {
        this.setState({repeatedPassword: event.target.value});
   }

    render() {
        return (
           <div>
               <form>
                   <div className="form-group">
                       <label htmlFor="username">Username</label>
                       <input type="email" className="form-control" id="username"
                              value={this.state.email} onChange={this.handleUsernameChange}
                              aria-describedby="usernameHelp"
                              placeholder="Enter username"/>
                   </div>

                   <div className="form-group">
                       <label htmlFor="email">Email address</label>
                       <input type="email" className="form-control" id="email"
                              aria-describedby="emailHelp"
                              placeholder="Enter email"/>
                   </div>

                   <div className="form-group">
                       <label htmlFor="password">Password</label>
                       <input type="password" className="form-control" id="password"
                              placeholder="Password"/>
                   </div>

                   <div className="form-group">
                       <label htmlFor="password">Repeat Password</label>
                       <input type="password" className="form-control" id="repeated_password"
                              placeholder="Repeat Password"/>
                   </div>

                   <button type="submit" className="btn btn-primary">Submit</button>
               </form>
           </div>
        );
    }
}