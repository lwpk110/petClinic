package cn.tendata.samples.petclinic.data.jpa.jackson;

import cn.tendata.samples.petclinic.data.jpa.jackson.DataView.User.Account;
import cn.tendata.samples.petclinic.data.jpa.jackson.DataView.User.Parent;
import cn.tendata.samples.petclinic.data.jpa.jackson.DataView.User.Profile;

public final class DataView {

    public interface Basic {}

    public interface Audit {}

    public static class User {

        public interface Login {}

        public interface Account {}

        public interface Profile {}

        public interface Parent {}

        public interface Default extends Basic, Login, Account, Profile {}
    }

    public static class Admin {

        public interface Default extends Basic, Audit {}

        public interface User extends Default, Account, Profile, Parent {}
    }
}
